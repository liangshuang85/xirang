package eco.ywhc.xr.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lark.oapi.Client;
import com.lark.oapi.service.approval.v4.model.CreateInstanceReq;
import com.lark.oapi.service.approval.v4.model.CreateInstanceResp;
import com.lark.oapi.service.approval.v4.model.InstanceCreate;
import eco.ywhc.xr.common.constant.ApprovalStatusType;
import eco.ywhc.xr.common.constant.ApprovalTemplateRefType;
import eco.ywhc.xr.common.converter.ApprovalConverter;
import eco.ywhc.xr.common.model.ApprovalForm;
import eco.ywhc.xr.common.model.entity.AdministrativeDivision;
import eco.ywhc.xr.common.model.entity.Approval;
import eco.ywhc.xr.common.model.entity.Clue;
import eco.ywhc.xr.core.manager.*;
import eco.ywhc.xr.core.mapper.ApprovalMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.sugar.commons.exception.InternalErrorException;
import org.sugar.commons.exception.InvalidInputException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private static final String FormName1 = "审批说明";

    private static final String FormName2 = "项目名称";

    private final AdministrativeDivisionManager administrativeDivisionManager;

    private final Client client;

    private final ApprovalMapper approvalMapper;

    private final ApprovalManager approvalManager;

    private final FrameworkAgreementManager frameworkAgreementManager;

    private final ProjectManager projectManager;

    private final ClueManager clueManager;

    private final ApprovalConverter approvalConverter;

    private final ObjectMapper objectMapper;

    @Value("${vendor.base-url}")
    private String baseUrl;

    @Override
    public int startLarkApproval(long id) {
        Approval currentApproval = approvalManager.mustFoundEntityById(id);
        if (currentApproval.getApprovalStatus() == ApprovalStatusType.PENDING) {
            throw new InvalidInputException("当前审批未完成");
        }

        List<ApprovalForm> approvalForms = approvalManager.getLarkApprovalForm(currentApproval);
        List<Map<String, String>> pendingCommits = new ArrayList<>();
        for (ApprovalForm form : approvalForms) {
            Map<String, String> pendingCommit = new HashMap<>();
            if (form.getType().equals("input")) {
                pendingCommit.put("id", form.getId());
                pendingCommit.put("type", form.getType());
                if (FormName1.equals(form.getName())) {
                    String path = switch (currentApproval.getRefType()) {
                        case CLUE -> "/ui/leadManagement/detail";
                        case FRAMEWORK_AGREEMENT -> "/ui/frameworkAgreement/detail";
                        case PROJECT -> "/ui/projectManagement/detail";
                    };
                    String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                            .replacePath(path)
                            .queryParam("id", currentApproval.getRefId())
                            .build()
                            .toUriString();
                    pendingCommit.put("value", "此审批为息壤机器人向" + currentApproval.getDepartmentName()
                            + "发起的自动审批请求，详情请见：" + url);
                    pendingCommits.add(pendingCommit);
                } else if (FormName2.equals(form.getName())) {
                    if (currentApproval.getRefType() == ApprovalTemplateRefType.CLUE) {
                        Clue clue = clueManager.findEntityById(currentApproval.getRefId());
                        pendingCommit.put("value", administrativeDivisionManager.findByAdcodeSurely(clue.getAdcode()).getName());
                    } else if (currentApproval.getRefType() == ApprovalTemplateRefType.FRAMEWORK_AGREEMENT) {
                        pendingCommit.put("value", frameworkAgreementManager.findEntityById(currentApproval.getRefId()).getName());
                    } else if (currentApproval.getRefType() == ApprovalTemplateRefType.PROJECT) {
                        pendingCommit.put("value", projectManager.findEntityById(currentApproval.getRefId()).getName());
                    }
                    pendingCommits.add(pendingCommit);
                }
            } else if (form.getType().equals("radioV2")) {
                pendingCommit.put("id", form.getId());
                pendingCommit.put("type", form.getType());
                for (ApprovalForm.OptionItem item : form.getOption()) {
                    Long adcode = approvalManager.getAdcodeByRefTypeAndRefId(currentApproval.getRefType(), currentApproval.getRefId());
                    Map<Short, AdministrativeDivision> administrativeDivisionMap = administrativeDivisionManager.analyzeByAdcode(adcode);
                    if (item.getText().equals(administrativeDivisionMap.get((short) (1)).getName())) {
                        pendingCommit.put("value", item.getValue());
                    }
                }
                pendingCommits.add(pendingCommit);
            }
        }

        String commits;
        try {
            commits = objectMapper.writeValueAsString(pendingCommits);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e);
        }
        CreateInstanceReq req = CreateInstanceReq.newBuilder()
                .instanceCreate(InstanceCreate.newBuilder()
                        .approvalCode(currentApproval.getApprovalCode())
                        .openId(approvalManager.getOpenIdByRefTypeAndRefId(currentApproval.getRefType(), currentApproval.getRefId()))
                        .form(commits)
                        .build())
                .build();
        CreateInstanceResp resp;
        try {
            resp = client.approval().instance().create(req);
            if (!resp.success()) {
                throw new InternalErrorException("审批发起失败");
            }
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
        String instanceCode = resp.getData().getInstanceCode();
        currentApproval.setApprovalInstanceId(instanceCode);
        // 根据审批状态更新审批
        if (currentApproval.getApprovalStatus() == ApprovalStatusType.PENDING_START) {
            approvalManager.updateApprovalFromLark(currentApproval);
            return 1;
        } else if (currentApproval.getApprovalStatus() == ApprovalStatusType.APPROVED ||
                currentApproval.getApprovalStatus() == ApprovalStatusType.REJECTED ||
                currentApproval.getApprovalStatus() == ApprovalStatusType.CANCELED ||
                currentApproval.getApprovalStatus() == ApprovalStatusType.DELETED) {
            Approval newApproval = currentApproval.toBuilder()
                    .id(null)
                    .build();
            approvalManager.updateApprovalFromLark(newApproval);
            approvalMapper.insert(newApproval);
            return 1;
        }
        return 0;
    }

}
