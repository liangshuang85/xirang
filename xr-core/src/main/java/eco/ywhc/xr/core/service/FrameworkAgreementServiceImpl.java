package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.*;
import eco.ywhc.xr.common.converter.FrameworkAgreementChannelEntryConverter;
import eco.ywhc.xr.common.converter.FrameworkAgreementConverter;
import eco.ywhc.xr.common.converter.TaskConverter;
import eco.ywhc.xr.common.event.FrameworkAgreementCreatedEvent;
import eco.ywhc.xr.common.event.FrameworkAgreementUpdatedEvent;
import eco.ywhc.xr.common.event.InstanceRoleLarkMemberInsertedEvent;
import eco.ywhc.xr.common.event.StatusChangedEvent;
import eco.ywhc.xr.common.exception.LarkTaskNotFoundException;
import eco.ywhc.xr.common.model.RequestContextUser;
import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementReq;
import eco.ywhc.xr.common.model.dto.res.*;
import eco.ywhc.xr.common.model.entity.FrameworkAgreement;
import eco.ywhc.xr.common.model.entity.FrameworkAgreementChannelEntry;
import eco.ywhc.xr.common.model.entity.InstanceRole;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.common.model.query.FrameworkAgreementQuery;
import eco.ywhc.xr.core.manager.*;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import eco.ywhc.xr.core.mapper.FrameworkAgreementChannelEntryMapper;
import eco.ywhc.xr.core.mapper.FrameworkAgreementMapper;
import eco.ywhc.xr.core.util.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;
import org.sugar.crud.model.PageableModelSet;

import java.time.OffsetDateTime;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FrameworkAgreementServiceImpl implements FrameworkAgreementService {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final FrameworkAgreementMapper frameworkAgreementMapper;

    private final FrameworkAgreementChannelEntryMapper frameworkAgreementChannelEntryMapper;

    private final FrameworkAgreementConverter frameworkAgreementConverter;

    private final FrameworkAgreementChannelEntryConverter frameworkAgreementChannelEntryConverter;

    private final AdministrativeDivisionManager administrativeDivisionManager;

    private final TaskConverter taskConverter;

    private final FrameworkAgreementManager frameworkAgreementManager;

    private final TaskManager taskManager;

    private final LarkEmployeeManager larkEmployeeManager;

    private final ApprovalManager approvalManager;

    private final VisitManager visitManager;

    private final AttachmentManager attachmentManager;

    private final InstanceRoleLarkMemberManager instanceRoleLarkMemberManager;

    private final InstanceRoleManager instanceRoleManager;

    private final ChangeManager changeManager;

    private final BasicDataManager basicDataManager;

    public String generateUniqueId() {
        QueryWrapper<FrameworkAgreement> qw = new QueryWrapper<>();
        qw.select("id", "code").orderByDesc("id").last("LIMIT 1");
        var frameworkAgreement = frameworkAgreementMapper.selectOne(qw);
        int num = 1;
        if (frameworkAgreement != null) {
            String numString = frameworkAgreement.getCode().substring(6);
            num = Integer.parseInt(numString) + 1;
        }
        String formattedNum = String.format("%03d", num);
        return "KJ" + Year.now() + formattedNum;
    }

    @Override
    public Long createOne(@NonNull FrameworkAgreementReq req) {
        FrameworkAgreement frameworkAgreement = frameworkAgreementConverter.fromRequest(req);
        frameworkAgreement.setCode(generateUniqueId());
        frameworkAgreement.setStatus(FrameworkAgreementType.PRE_PROJECT);
        frameworkAgreementMapper.insert(frameworkAgreement);
        // 比较并更新附件
        frameworkAgreementManager.compareAndUpdateAttachments(req, frameworkAgreement.getId());
        // 判断项目建议书批复状态和框架协议书签署状态
        frameworkAgreementManager.compareAndUpdateStatus(frameworkAgreement);

        FrameworkAgreementChannelEntry frameworkAgreementChannelEntry = frameworkAgreementChannelEntryConverter.fromRequest(req.getFrameworkAgreementChannelEntry());
        frameworkAgreementChannelEntry.setFrameworkAgreementId(frameworkAgreement.getId());
        frameworkAgreementChannelEntryMapper.insert(frameworkAgreementChannelEntry);
        frameworkAgreementManager.compareAndUpdateAttachments(req.getFrameworkAgreementChannelEntry(), frameworkAgreementChannelEntry.getId());

        //插入基本数据
        basicDataManager.createOne(req.getBasicData(), frameworkAgreement.getId());

        visitManager.createMany(req.getFrameworkVisits(), frameworkAgreement.getId());

        if (CollectionUtils.isNotEmpty(req.getInstanceRoleLarkMembers())) {
            instanceRoleLarkMemberManager.insertInstanceRoleLarkMember(req.getInstanceRoleLarkMembers(), frameworkAgreement.getId(), InstanceRefType.FRAMEWORK_AGREEMENT);
            List<String> memberIds = instanceRoleLarkMemberManager.getMemberIdsByRefId(frameworkAgreement.getId());
            applicationEventPublisher.publishEvent(InstanceRoleLarkMemberInsertedEvent.of(frameworkAgreement.getId(), req.getName(), TaskTemplateRefType.FRAMEWORK_AGREEMENT, memberIds));
        }

        applicationEventPublisher.publishEvent(FrameworkAgreementCreatedEvent.of(frameworkAgreement));

        StatusChangedEvent statusChangedEvent = StatusChangedEvent.builder()
                .refId(frameworkAgreement.getId())
                .refType(InstanceRefType.FRAMEWORK_AGREEMENT)
                .before("")
                .after(frameworkAgreement.getStatus().name())
                .operatorId(frameworkAgreement.getAssigneeId())
                .lastModifiedAt(frameworkAgreement.getCreatedAt())
                .build();
        applicationEventPublisher.publishEvent(statusChangedEvent);

        return frameworkAgreement.getId();
    }

    @Override
    public PageableModelSet<FrameworkAgreementRes> findMany(@NonNull FrameworkAgreementQuery query) {
        RequestContextUser requestContextUser = SessionUtils.currentUser();

        List<Long> adIds = new ArrayList<>();
        if (query.getAdcode() != null) {
            adIds = administrativeDivisionManager.findAllEntityIdsSince(query.getAdcode());
            if (CollectionUtils.isEmpty(adIds)) {
                return PageableModelSet.from(query.paging());
            }
        }
        QueryWrapper<FrameworkAgreement> qw = new QueryWrapper<>();
        qw.lambda().eq(FrameworkAgreement::getDeleted, 0)
                .eq(StringUtils.isNotBlank(query.getAssigneeId()), FrameworkAgreement::getAssigneeId, query.getAssigneeId())
                .eq(Objects.nonNull(query.getStatus()), FrameworkAgreement::getStatus, query.getStatus())
                .like(StringUtils.isNotBlank(query.getName()), FrameworkAgreement::getName, query.getName())
                .in(CollectionUtils.isNotEmpty(adIds), FrameworkAgreement::getAdcode, adIds)
                .orderByDesc(FrameworkAgreement::getId);

        // 如果没有全局 FRAMEWORK_AGREEMENT:VIEW 权限，则检查实例权限
        if (!SessionUtils.currentUserPermissionCodes().contains("FRAMEWORK_AGREEMENT:VIEW")) {
            String existsStatement = "SELECT 1 FROM `s_instance_role_lark_member` WHERE `deleted`=0 " +
                    " AND `member_id`='" + requestContextUser.getLarkOpenId() + "' " +
                    " AND `ref_type`='FRAMEWORK_AGREEMENT' " +
                    " AND `ref_id`=`b_framework_agreement`.id";
            qw.exists(existsStatement);
        }

        var rows = frameworkAgreementMapper.selectPage(query.paging(true), qw);
        if (CollectionUtils.isEmpty(rows.getRecords())) {
            return PageableModelSet.from(query.paging());
        }

        Set<Long> adcodes = rows.getRecords().stream().map(FrameworkAgreement::getAdcode).collect(Collectors.toSet());
        Map<Long, AdministrativeDivisionRes> administrativeDivisionMap = administrativeDivisionManager.findAllAsMapByAdcodesSurely(adcodes);

        List<Long> clueIds = rows.getRecords().stream().map(FrameworkAgreement::getId).toList();
        Map<Long, Set<String>> permissionMap = instanceRoleManager.listPermissionCodesByRefIdsAndMemberId(clueIds, requestContextUser.getLarkOpenId());

        var results = rows.convert(i -> {
            FrameworkAgreementRes res = frameworkAgreementConverter.toResponse(i);
            res.setAdministrativeDivision(administrativeDivisionMap.get(i.getAdcode()));

            LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployee(i.getAssigneeId());
            AssigneeRes assignee = AssigneeRes.builder()
                    .assigneeId(i.getAssigneeId())
                    .assigneeName(larkEmployee.getName())
                    .avatarInfo(larkEmployee.getAvatarInfo())
                    .build();
            res.setAssignee(assignee);

            FrameworkAgreementChannelEntryRes channelEntry = frameworkAgreementManager.getChannelEntryByFrameworkAgreementId(i.getId());
            res.setFrameworkAgreementChannelEntry(channelEntry);

            res.setPermissionCodes(permissionMap.getOrDefault(i.getId(), Collections.emptySet()));

            return res;
        });
        return PageableModelSet.from(results);
    }

    @Override
    public FrameworkAgreementRes findOne(@NonNull Long id) {
        RequestContextUser requestContextUser = SessionUtils.currentUser();

        FrameworkAgreement frameworkAgreement = frameworkAgreementManager.mustFoundEntityById(id);
        FrameworkAgreementRes res = frameworkAgreementConverter.toResponse(frameworkAgreement);
        List<AttachmentResponse> projectProposalAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.PROJECT_PROPOSAL);
        List<AttachmentResponse> projectProposalApprovalAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.PROJECT_PROPOSAL_APPROVAL);
        List<AttachmentResponse> meetingResolutionAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.MEETING_RESOLUTION);
        List<AttachmentResponse> meetingMinutesAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.MEETING_MINUTES);
        List<AttachmentResponse> frameworkAgreementAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.FRAMEWORK_AGREEMENT);
        List<AttachmentResponse> frameworkAgreementSigningAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.FRAMEWORK_AGREEMENT_SIGNING);
        res.setProjectProposalAttachments(projectProposalAttachments);
        res.setProjectProposalApprovalAttachments(projectProposalApprovalAttachments);
        res.setMeetingResolutionAttachments(meetingResolutionAttachments);
        res.setMeetingMinutesAttachments(meetingMinutesAttachments);
        res.setFrameworkAgreementAttachments(frameworkAgreementAttachments);
        res.setFrameworkAgreementSigningAttachments(frameworkAgreementSigningAttachments);

        AdministrativeDivisionRes administrativeDivision = administrativeDivisionManager.findByAdcodeSurely(frameworkAgreement.getAdcode());
        res.setAdministrativeDivision(administrativeDivision);

        LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployee(frameworkAgreement.getAssigneeId());
        AssigneeRes assignee = AssigneeRes.builder()
                .assigneeId(frameworkAgreement.getAssigneeId())
                .assigneeName(larkEmployee.getName())
                .avatarInfo(larkEmployee.getAvatarInfo())
                .build();
        res.setAssignee(assignee);

        FrameworkAgreementChannelEntryRes channelEntry = frameworkAgreementManager.getChannelEntryByFrameworkAgreementId(frameworkAgreement.getId());
        res.setFrameworkAgreementChannelEntry(channelEntry);

        // 查询基础数据
        res.setBasicData(basicDataManager.getBasicData(id));

        // 查询实例角色成员
        List<InstanceRoleLarkMemberRes> instanceRoleLarkMembers = instanceRoleLarkMemberManager.findInstanceRoleLarkMemberByRefId(id);
        res.setInstanceRoleLarkMembers(instanceRoleLarkMembers);

        List<Task> tasks = taskManager.listTasksByRefId(id);
        List<TaskRes> taskResList = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTaskGuid() == null) {
                InstanceRole instanceRole = instanceRoleManager.findEntityById(task.getInstanceRoleId());
                TaskRes taskRes = taskConverter.toResponse(task);
                // 如果任务状态为待发起，则显示实例角色成员
                if (task.getStatus() == TaskStatusType.pending) {
                    Set<String> memberIds = instanceRoleLarkMembers.stream()
                            .filter(i -> i.getInstanceRoleId().equals(task.getInstanceRoleId()))
                            .findFirst()
                            .map(InstanceRoleLarkMemberRes::getMemberIds)
                            .orElse(Collections.emptySet());
                    if (!memberIds.isEmpty()) {
                        List<AssigneeRes> assignees = memberIds.stream()
                                .map(memberId -> {
                                    LarkEmployee larkEmployee1 = larkEmployeeManager.retrieveLarkEmployee(memberId);
                                    return AssigneeRes.builder()
                                            .assigneeId(memberId)
                                            .assigneeName(larkEmployee1.getName())
                                            .avatarInfo(larkEmployee1.getAvatarInfo())
                                            .build();
                                })
                                .toList();
                        taskRes.setAssignees(assignees);
                    }
                }
                taskRes.setInstanceRoleName(instanceRole.getName());
                taskResList.add(taskRes);
                continue;
            }
            try {
                TaskRes larkTask = taskManager.getLarkTask(task);
                // 获取任务负责人
                List<String> memberIds = instanceRoleLarkMemberManager.getMemberIdsByInstanceRoleIdAndRefId(task.getInstanceRoleId(), id);
                // 获取任务负责人信息
                List<AssigneeRes> assignees = memberIds.stream()
                        .map(memberId -> {
                            LarkEmployee larkEmployee1 = larkEmployeeManager.retrieveLarkEmployee(memberId);
                            return AssigneeRes.builder()
                                    .assigneeId(memberId)
                                    .assigneeName(larkEmployee1.getName())
                                    .avatarInfo(larkEmployee1.getAvatarInfo())
                                    .build();
                        })
                        .toList();
                larkTask.setAssignees(assignees);
                taskResList.add(larkTask);
            } catch (LarkTaskNotFoundException ignored) {
                task.setTaskGuid(null);
                task.setStatus(TaskStatusType.deleted);
                taskManager.updateById(task);
            } catch (Exception e) {
                throw new InternalErrorException("查询飞书任务失败");
            }
        }
        Map<TaskType, Map<String, List<TaskRes>>> taskMap = taskResList.stream()
                .collect(Collectors.groupingBy(
                        TaskRes::getType,
                        Collectors.groupingBy(TaskRes::getInstanceRoleName)
                ));
        res.setTaskMap(taskMap);

        Map<ApprovalType, Map<String, List<ApprovalRes>>> approvalResMaps = approvalManager.listApprovalsByRefId(id).stream()
                .peek(i -> {
                    if (i.getApprovalInstanceId() != null) {
                        approvalManager.updateApproval(i);
                    }
                })
                .collect(Collectors.groupingBy(
                        ApprovalRes::getType,
                        Collectors.groupingBy(ApprovalRes::getDepartmentName)
                ));
        res.setApprovalMap(approvalResMaps);

        List<VisitRes> visitList = visitManager.findAllByRefId(id);
        res.setFrameworkVisits(visitList);

        List<ChangeRes> changes = changeManager.findAllByRefId(id);
        List<ChangeRes> changeRes = changes.stream().peek(i -> i.setOperator(assignee)).toList();
        res.setChanges(changeRes);

        res.setPermissionCodes(instanceRoleManager.listPermissionCodesByRefIdAndMemberId(id, requestContextUser.getLarkOpenId()));

        return res;
    }

    @Override
    public int updateOne(@NonNull Long id, @NonNull FrameworkAgreementReq req) {
        FrameworkAgreement frameworkAgreement = frameworkAgreementManager.mustFoundEntityById(id);
        OffsetDateTime updatedAt = frameworkAgreement.getUpdatedAt();
        FrameworkAgreementType currentStatus = frameworkAgreement.getStatus();
        frameworkAgreementConverter.update(req, frameworkAgreement);
        frameworkAgreementManager.compareAndUpdateAttachments(req, frameworkAgreement.getId());
        int affected = frameworkAgreementMapper.updateById(frameworkAgreement);

        // 判断项目建议书批复状态
        boolean projectProposalApproved = !attachmentManager.findManyEntitiesByOwnerId(frameworkAgreement.getId(), FileOwnerType.PROJECT_PROPOSAL_APPROVAL).isEmpty();
        if (projectProposalApproved) {
            frameworkAgreement.setProjectProposalApproved(true);
            frameworkAgreementMapper.updateById(frameworkAgreement);
        }
        // 判断框架协议书签署状态
        boolean frameworkAgreementSigned = !attachmentManager.findManyEntitiesByOwnerId(frameworkAgreement.getId(), FileOwnerType.FRAMEWORK_AGREEMENT_SIGNING).isEmpty();
        if (frameworkAgreementSigned) {
            frameworkAgreement.setFrameworkAgreementSigned(true);
            frameworkAgreementMapper.updateById(frameworkAgreement);
        }

        FrameworkAgreementChannelEntry frameworkAgreementChannelEntry = frameworkAgreementManager.getFrameworkAgreementChannelEntryById(id);
        frameworkAgreementChannelEntryConverter.update(req.getFrameworkAgreementChannelEntry(), frameworkAgreementChannelEntry);
        frameworkAgreementChannelEntry.setFrameworkAgreementId(id);
        // 比较并更新附件
        frameworkAgreementManager.compareAndUpdateAttachments(req.getFrameworkAgreementChannelEntry(), frameworkAgreementChannelEntry.getId());
        // 判断项目建议书批复状态和框架协议书签署状态
        frameworkAgreementManager.compareAndUpdateStatus(frameworkAgreement);
        frameworkAgreementChannelEntryMapper.updateById(frameworkAgreementChannelEntry);

        //更新基础数据
        basicDataManager.updateOne(req.getBasicData(), id);

        visitManager.logicDeleteAllEntitiesByRefId(id);
        visitManager.createMany(req.getFrameworkVisits(), id);

        instanceRoleLarkMemberManager.deleteInstanceRoleLarkMember(id);
        if (CollectionUtils.isNotEmpty(req.getInstanceRoleLarkMembers())) {
            instanceRoleLarkMemberManager.insertInstanceRoleLarkMember(req.getInstanceRoleLarkMembers(), id, InstanceRefType.FRAMEWORK_AGREEMENT);
        }
        List<String> memberIds = instanceRoleLarkMemberManager.getMemberIdsByRefId(frameworkAgreement.getId());
        applicationEventPublisher.publishEvent(InstanceRoleLarkMemberInsertedEvent.of(id, req.getName(), TaskTemplateRefType.FRAMEWORK_AGREEMENT, memberIds));

        // 发布框架协议已更新事件
        applicationEventPublisher.publishEvent(FrameworkAgreementUpdatedEvent.of(frameworkAgreement));

        // 发布状态变更事件
        if (currentStatus != req.getStatus()) {
            StatusChangedEvent statusChangedEvent = StatusChangedEvent.builder()
                    .refId(frameworkAgreement.getId())
                    .refType(InstanceRefType.FRAMEWORK_AGREEMENT)
                    .before(currentStatus.name())
                    .after(req.getStatus().name())
                    .operatorId(frameworkAgreement.getAssigneeId())
                    .lastModifiedAt(updatedAt)
                    .build();
            applicationEventPublisher.publishEvent(statusChangedEvent);
        }

        return affected;
    }

    @Override
    public int logicDeleteOne(@NonNull Long id) {
        attachmentManager.deleteByOwnerId(id);
        attachmentManager.deleteByOwnerId(frameworkAgreementManager.getChannelEntryByFrameworkAgreementId(id).getId());
        frameworkAgreementChannelEntryMapper.logicDeleteEntityById(frameworkAgreementManager.getChannelEntryByFrameworkAgreementId(id).getId());
        taskManager.logicDeleteEntityById(id);
        approvalManager.logicDeleteAllEntitiesByRefId(id);
        visitManager.logicDeleteAllEntitiesByRefId(id);
        changeManager.bulkDeleteByRefId(id);
        // 逻辑删除基础信息
        basicDataManager.deleteEntityByRefId(id);
        return frameworkAgreementMapper.logicDeleteEntityById(id);
    }

}
