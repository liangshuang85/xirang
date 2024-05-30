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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;
import org.sugar.commons.exception.ResourceNotFoundException;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 审批记录(eco.ywhc.xr.common.model.entity.BApproval)表服务实现类
 *
 * @author makejava
 * @since 2024-04-18 11:51:08
 */
@Service
@RequiredArgsConstructor
@Slf4j
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
    public String getLarkApprovalMembers(String instanceCode) {
        GetInstanceReq req = GetInstanceReq.newBuilder()
                .instanceId(instanceCode)
                .build();
        GetInstanceResp resp;
        try {
            resp = client.approval().instance().get(req);
        } catch (Exception e) {
            log.error("获取审批实例失败:{}", e.getMessage());
            throw new InternalErrorException(e);
        }
        if (!resp.success()) {
            log.error("获取审批实例失败:{}", resp.getMsg());
            throw new InternalErrorException("获取审批实例失败");
        }
        // 获取审批实例的待审批人
        return Arrays.stream(resp.getData().getTaskList())
                .filter(instanceTask -> instanceTask.getStatus().equals("PENDING"))
                .map(InstanceTask::getOpenId)
                .collect(Collectors.joining(","));
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
                .map(i -> {
                    ApprovalRes res = approvalConverter.toResponse(i);
                    List<AssigneeRes> assignees = new ArrayList<>();
                    if (StringUtils.isNotBlank(i.getMembers())) {
                        Set<String> memberIds = Arrays.stream(i.getMembers().split(","))
                                .map(String::valueOf)
                                .collect(Collectors.toSet());
                        assignees = memberIds.stream()
                                .map(assigneeId -> {
                                    LarkEmployee approvalLarkEmployee = larkEmployeeManager.retrieveLarkEmployee(assigneeId);
                                    return AssigneeRes.builder()
                                            .assigneeId(assigneeId)
                                            .assigneeName(approvalLarkEmployee.getName())
                                            .avatarInfo(approvalLarkEmployee.getAvatarInfo())
                                            .build();
                                })
                                .toList();
                    }
                    res.setAssignees(assignees);
                    if (res.getApprovalStatus() == ApprovalStatusType.PENDING) {
                        res.setPendingAssignees(assignees);
                    }
                    if (StringUtils.isNotBlank(i.getInstanceTasks())) {
                        List<String> appLinks = Arrays.stream(i.getInstanceTasks().split(","))
                                .map(instanceTask -> "https://applink.feishu.cn/client/mini_program/open?appId=cli_9cb844403dbb9108&mode=appCenter&path=pc/pages/in-process/index?enableTrusteeship=true&instanceId="
                                        + instanceTask + "&source=approval_bot&relaunch=true")
                                .toList();
                        res.setAppLinks(appLinks);
                    }
                    res.setStartTime(i.getStartTime());
                    res.setEndTime(i.getEndTime());
                    return res;
                })
                .toList();
    }

    @Override
    public void updateApprovalFromLark(Approval approval) {
        if (StringUtils.isBlank(approval.getApprovalInstanceId())) {
            return;
        }
        GetInstanceReq req = GetInstanceReq.newBuilder()
                .instanceId(approval.getApprovalInstanceId())
                .build();
        GetInstanceResp resp;
        try {
            resp = client.approval().instance().get(req);
        } catch (Exception e) {
            log.error("获取审批实例失败:{}", e.getMessage());
            throw new InternalErrorException(e);
        }
        if (!resp.success()) {
            log.error("获取审批实例失败:{}", resp.getMsg());
            throw new InternalErrorException("获取审批实例失败");
        }
        GetInstanceRespBody approvalData = resp.getData();
        ApprovalStatusType approvalStatusType = ApprovalStatusType.valueOf(approvalData.getStatus());
        // 如果审批状态不一致则更新
        if (approval.getApprovalStatus() != approvalStatusType) {
            approval.setApprovalStatus(approvalStatusType);
            approvalMapper.updateById(approval);
        }
        if (approvalStatusType != ApprovalStatusType.PENDING) {
            approval.setApprovalInstanceId(null);
        }
        InstanceTask[] taskList = approvalData.getTaskList();
        // 获取审批实例的待审批人
        approval.setMembers(Arrays.stream(taskList)
                .filter(instanceTask -> approval.getApprovalStatus().name().equals(instanceTask.getStatus()))
                .map(InstanceTask::getOpenId)
                .collect(Collectors.joining(",")));
        // 获取审批实例的开始时间和结束时间
        if (approvalStatusType == ApprovalStatusType.APPROVED || approvalStatusType == ApprovalStatusType.REJECTED) {
            Instant startTime = Instant.ofEpochMilli(Long.parseLong(approvalData.getStartTime()));
            approval.setStartTime(startTime.atOffset(ZoneOffset.ofHours(8)));
            Instant endTime = Instant.ofEpochMilli(Long.parseLong(approvalData.getEndTime()));
            approval.setEndTime(endTime.atOffset(ZoneOffset.ofHours(8)));
        }
        // 获取审批实例的审批任务ID
        final String approvalInstanceId = Arrays.stream(taskList)
                .filter(instanceTask -> approval.getApprovalStatus().name().equals(instanceTask.getStatus()))
                .map(InstanceTask::getId)
                .collect(Collectors.joining(","));
        approval.setInstanceTasks(approvalInstanceId);
        approvalMapper.updateById(approval);
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
