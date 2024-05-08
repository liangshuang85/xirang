package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.lark.oapi.Client;
import com.lark.oapi.service.approval.v4.model.*;
import eco.ywhc.xr.common.constant.ApprovalStatusType;
import eco.ywhc.xr.common.constant.ApprovalTemplateRefType;
import eco.ywhc.xr.common.converter.ApprovalConverter;
import eco.ywhc.xr.common.model.ApprovalForm;
import eco.ywhc.xr.common.model.dto.res.ApprovalRes;
import eco.ywhc.xr.common.model.dto.res.AssigneeRes;
import eco.ywhc.xr.common.model.entity.Approval;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import eco.ywhc.xr.core.mapper.ApprovalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;
import org.sugar.commons.exception.ResourceNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 审批记录(eco.ywhc.xr.common.model.entity.BApproval)表服务实现类
 *
 * @author makejava
 * @since 2024-04-18 11:51:08
 */
@Service
@RequiredArgsConstructor
public class ApprovalManagerImpl implements ApprovalManager {

    private final Client client;

    private final ApprovalMapper approvalMapper;

    private final ApprovalConverter approvalConverter;

    private final ClueManager clueManager;

    private final FrameworkAgreementManager frameworkAgreementManager;

    private final ProjectManager projectManager;

    private final LarkEmployeeManager larkEmployeeManager;

    private final ObjectMapper objectMapper;

    @Override
    public Approval findEntityById(long id) {
        return approvalMapper.findEntityById(id);
    }

    @Override
    public Approval mustFoundEntityById(long id) {
        return Optional.ofNullable(findEntityById(id)).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<Approval> findAllEntitiesByClueId(long clueId) {
        QueryWrapper<Approval> qw = new QueryWrapper<>();
        qw.lambda().eq(Approval::getDeleted, 0)
                .eq(Approval::getRefId, clueId);
        return approvalMapper.selectList(qw);
    }

    @Override
    public List<ApprovalRes> findAllByClueId(long clueId) {
        return findAllEntitiesByClueId(clueId).stream().map(approvalConverter::toResponse).toList();
    }

    @Override
    public void logicDeleteAllEntitiesByRefId(long refId) {
        UpdateWrapper<Approval> uw = new UpdateWrapper<>();
        uw.lambda().eq(Approval::getDeleted, false)
                .eq(Approval::getRefId, refId)
                .set(Approval::getDeleted, true);
        approvalMapper.update(null, uw);
    }

    @Override
    public List<ApprovalRes> listApprovalsByRefId(long id) {
        QueryWrapper<Approval> qw = new QueryWrapper<>();
        qw.lambda().eq(Approval::getDeleted, 0)
                .eq(Approval::getRefId, id)
                .orderByDesc(Approval::getId);
        List<Approval> approvals = approvalMapper.selectList(qw);
        return approvals.stream()
                .map(approvalConverter::toResponse)
                .toList();
    }

    @Override
    public void updateApproval(ApprovalRes res) {
        GetInstanceReq req = GetInstanceReq.newBuilder()
                .instanceId(res.getApprovalInstanceId())
                .build();
        GetInstanceResp resp;
        try {
            resp = client.approval().instance().get(req);
            if (!resp.success()) {
                throw new InternalErrorException("获取审批实例失败");
            }
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }
        GetInstanceRespBody approvalData = resp.getData();
        ApprovalStatusType approvalStatusType = ApprovalStatusType.valueOf(approvalData.getStatus());
        if (res.getApprovalStatus() != approvalStatusType) {
            res.setApprovalStatus(approvalStatusType);
            Approval entity = approvalMapper.findEntityById(res.getId());
            entity.setApprovalStatus(approvalStatusType);
            approvalMapper.updateById(entity);
        }

        InstanceTask[] taskList = approvalData.getTaskList();
        List<String> assigneeIds = Arrays.stream(taskList)
                .filter(instanceTask -> ApprovalStatusType.PENDING.name().equals(instanceTask.getStatus()))
                .map(InstanceTask::getOpenId)
                .toList();
        List<AssigneeRes> assignees = assigneeIds.stream()
                .map(assigneeId -> {
                    LarkEmployee approvalLarkEmployee = larkEmployeeManager.retrieveLarkEmployee(assigneeId);
                    return AssigneeRes.builder()
                            .assigneeId(assigneeId)
                            .assigneeName(approvalLarkEmployee.getName())
                            .avatarInfo(approvalLarkEmployee.getAvatarInfo())
                            .build();
                })
                .toList();
        res.setAssignees(assignees);

        List<String> appLinks = Arrays.stream(taskList)
                .filter(instanceTask -> ApprovalStatusType.PENDING.name().equals(instanceTask.getStatus()))
                .map(instanceTask ->
                        "https://applink.feishu.cn/client/mini_program/open?appId=cli_9cb844403dbb9108&mode=appCenter&path=pc/pages/in-process/index?enableTrusteeship=true&instanceId="
                                + instanceTask.getId() + "&source=approval_bot&relaunch=true")
                .toList();
        res.setAppLinks(appLinks);
    }

    @Override
    public List<ApprovalForm> getLarkApprovalForm(Approval approval) {
        GetApprovalReq req = GetApprovalReq.newBuilder()
                .approvalCode(approval.getApprovalCode())
                .build();
        GetApprovalResp resp;

        try {
            resp = client.approval().approval().get(req);
            if (!resp.success()) {
                throw new InternalErrorException("获取审批定义失败");
            }
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }

        CollectionType mapCollectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, ApprovalForm.class);
        try {
            return objectMapper.readValue(resp.getData().getForm(), mapCollectionType);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Long getAdcodeByRefTypeAndRefId(@NonNull ApprovalTemplateRefType refType, long refId) {
        return switch (refType) {
            case CLUE -> clueManager.findEntityById(refId).getAdcode();
            case FRAMEWORK_AGREEMENT -> frameworkAgreementManager.findEntityById(refId).getAdcode();
            case PROJECT -> projectManager.findEntityById(refId).getAdcode();
        };
    }

    @Override
    public String getOpenIdByRefTypeAndRefId(ApprovalTemplateRefType refType, long refId) {
        return switch (refType) {
            case CLUE -> clueManager.findEntityById(refId).getAssigneeId();
            case FRAMEWORK_AGREEMENT -> frameworkAgreementManager.findEntityById(refId).getAssigneeId();
            case PROJECT -> projectManager.findEntityById(refId).getAssigneeId();
        };

    }

    @Override
    public List<Approval> listApprovals(long prevApprovalId, int limit) {
        QueryWrapper<Approval> qw = new QueryWrapper<>();
        qw.lambda().eq(Approval::getDeleted, 0)
                .eq(Approval::getApprovalStatus, ApprovalStatusType.PENDING)
                .gt(Approval::getId, prevApprovalId)
                .orderByAsc(Approval::getId)
                .last("LIMIT " + limit);
        return approvalMapper.selectList(qw);
    }

}
