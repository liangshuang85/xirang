package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.ApprovalType;
import eco.ywhc.xr.common.constant.FileOwnerType;
import eco.ywhc.xr.common.constant.FrameworkAgreementType;
import eco.ywhc.xr.common.constant.TaskType;
import eco.ywhc.xr.common.converter.*;
import eco.ywhc.xr.common.event.FrameworkAgreementCreatedEvent;
import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementReq;
import eco.ywhc.xr.common.model.dto.res.*;
import eco.ywhc.xr.common.model.entity.*;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.common.model.query.FrameworkAgreementQuery;
import eco.ywhc.xr.core.manager.*;
import eco.ywhc.xr.core.manager.lark.LarkDepartmentManager;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import eco.ywhc.xr.core.mapper.FrameworkAgreementChannelEntryMapper;
import eco.ywhc.xr.core.mapper.FrameworkAgreementMapper;
import eco.ywhc.xr.core.mapper.FrameworkAgreementProjectFundingMapper;
import eco.ywhc.xr.core.mapper.FrameworkAgreementProjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;
import org.sugar.crud.model.PageableModelSet;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FrameworkAgreementServiceImpl implements FrameworkAgreementService {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final FrameworkAgreementMapper frameworkAgreementMapper;

    private final FrameworkAgreementProjectMapper frameworkAgreementProjectMapper;

    private final FrameworkAgreementProjectFundingMapper frameworkAgreementProjectFundingMapper;

    private final FrameworkAgreementChannelEntryMapper frameworkAgreementChannelEntryMapper;

    private final FrameworkAgreementConverter frameworkAgreementConverter;

    private final FrameworkAgreementChannelEntryConverter frameworkAgreementChannelEntryConverter;

    private final FrameworkAgreementProjectFundingConverter frameworkAgreementProjectFundingConverter;

    private final FrameworkAgreementProjectConverter frameworkAgreementProjectConverter;

    private final AdministrativeDivisionManager administrativeDivisionManager;

    private final TaskConverter taskConverter;

    private final FrameworkAgreementManager frameworkAgreementManager;

    private final TaskManager taskManager;

    private final LarkEmployeeManager larkEmployeeManager;

    private final ApprovalManager approvalManager;

    private final VisitManager visitManager;

    private final AttachmentManager attachmentManager;

    private final LarkDepartmentManager larkDepartmentManager;

    private final InstanceRoleLarkMemberManager instanceRoleLarkMemberManager;

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
        frameworkAgreementManager.compareAndUpdateAttachments(req, frameworkAgreement.getId());

        FrameworkAgreementChannelEntry frameworkAgreementChannelEntry = frameworkAgreementChannelEntryConverter.fromRequest(req.getFrameworkAgreementChannelEntry());
        frameworkAgreementChannelEntry.setFrameworkAgreementId(frameworkAgreement.getId());
        frameworkAgreementChannelEntryMapper.insert(frameworkAgreementChannelEntry);
        frameworkAgreementManager.compareAndUpdateAttachments(req.getFrameworkAgreementChannelEntry(), frameworkAgreementChannelEntry.getId());

        FrameworkAgreementProjectFunding frameworkAgreementProjectFunding = frameworkAgreementProjectFundingConverter.fromRequest(req.getFrameworkAgreementProjectFunding());
        frameworkAgreementProjectFunding.setFrameworkAgreementId(frameworkAgreement.getId());
        frameworkAgreementProjectFundingMapper.insert(frameworkAgreementProjectFunding);
        frameworkAgreementManager.compareAndUpdateAttachments(req.getFrameworkAgreementProjectFunding(), frameworkAgreementProjectFunding.getId());

        FrameworkAgreementProject frameworkAgreementProject = frameworkAgreementProjectConverter.fromRequest(req.getFrameworkAgreementProject());
        frameworkAgreementProject.setFrameworkAgreementId(frameworkAgreement.getId());
        frameworkAgreementProjectMapper.insert(frameworkAgreementProject);

        visitManager.createMany(req.getFrameworkVisits(), frameworkAgreement.getId());
        instanceRoleLarkMemberManager.insertInstanceRoleLarkMember(req, frameworkAgreement.getId());

        applicationEventPublisher.publishEvent(FrameworkAgreementCreatedEvent.of(frameworkAgreement));

        return frameworkAgreement.getId();
    }

    @Override
    public PageableModelSet<FrameworkAgreementRes> findMany(@NonNull FrameworkAgreementQuery query) {
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
                .in(CollectionUtils.isNotEmpty(adIds), FrameworkAgreement::getAdcode, adIds)
                .orderByDesc(FrameworkAgreement::getId);

        var rows = frameworkAgreementMapper.selectPage(query.paging(true), qw);
        if (CollectionUtils.isEmpty(rows.getRecords())) {
            return PageableModelSet.from(query.paging());
        }

        Set<Long> adcodes = rows.getRecords().stream().map(FrameworkAgreement::getAdcode).collect(Collectors.toSet());
        Map<Long, AdministrativeDivisionRes> administrativeDivisionMap = administrativeDivisionManager.findAllAsMapByAdcodesSurely(adcodes);

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

            FrameworkAgreementProjectRes project = frameworkAgreementManager.getProjectByFrameworkAgreementId(i.getId());
            res.setFrameworkAgreementProject(project);

            FrameworkAgreementProjectFundingRes funding = frameworkAgreementManager.getProjectFundingByFrameworkAgreementId(i.getId());
            res.setFrameworkAgreementProjectFunding(funding);

            FrameworkAgreementChannelEntryRes channelEntry = frameworkAgreementManager.getChannelEntryByFrameworkAgreementId(i.getId());
            res.setFrameworkAgreementChannelEntry(channelEntry);

            return res;
        });
        return PageableModelSet.from(results);
    }

    @Override
    public FrameworkAgreementRes findOne(@NonNull Long id) {
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

        FrameworkAgreementProjectRes project = frameworkAgreementManager.getProjectByFrameworkAgreementId(frameworkAgreement.getId());
        res.setFrameworkAgreementProject(project);

        FrameworkAgreementProjectFundingRes funding = frameworkAgreementManager.getProjectFundingByFrameworkAgreementId(frameworkAgreement.getId());
        res.setFrameworkAgreementProjectFunding(funding);

        FrameworkAgreementChannelEntryRes channelEntry = frameworkAgreementManager.getChannelEntryByFrameworkAgreementId(frameworkAgreement.getId());
        res.setFrameworkAgreementChannelEntry(channelEntry);

        List<Task> tasks = taskManager.listTasksByRefId(id);
        List<TaskRes> taskResList = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTaskGuid() == null) {
                TaskRes taskRes = taskConverter.toResponse(task);
                taskResList.add(taskRes);
                continue;
            }
            try {
                TaskRes larkTask = taskManager.getLarkTask(task);
                DepartmentRes department = larkDepartmentManager.getDepartmentByDepartmentId(task.getDepartmentId());
                String leaderUserId = department.getLeaderUserId();
                if (StringUtils.isNotBlank(leaderUserId)) {
                    LarkEmployee taskLarkEmployee = larkEmployeeManager.retrieveLarkEmployee(leaderUserId);
                    AssigneeRes taskAssignee = AssigneeRes.builder()
                            .assigneeId(leaderUserId)
                            .assigneeName(taskLarkEmployee.getName())
                            .avatarInfo(taskLarkEmployee.getAvatarInfo())
                            .build();
                    larkTask.setAssignee(taskAssignee);
                }
                taskResList.add(larkTask);
            } catch (Exception e) {
                throw new InternalErrorException("查询飞书任务失败");
            }
        }
        Map<TaskType, Map<String, List<TaskRes>>> taskMap = taskResList.stream()
                .collect(Collectors.groupingBy(
                        TaskRes::getType,
                        Collectors.groupingBy(TaskRes::getDepartmentName)
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

        List<InstanceRoleLarkMemberRes> instanceRoleLarkMembers = instanceRoleLarkMemberManager.findInstanceRoleLarkMemberByRefId(id);
        res.setInstanceRoleLarkMembers(instanceRoleLarkMembers);

        return res;
    }

    @Override
    public int updateOne(@NonNull Long id, @NonNull FrameworkAgreementReq req) {
        FrameworkAgreement frameworkAgreement = frameworkAgreementManager.mustFoundEntityById(id);
        if (req.getStatus() == null) {
            req.setStatus(frameworkAgreement.getStatus());
        }
        frameworkAgreementConverter.update(req, frameworkAgreement);
        frameworkAgreementManager.compareAndUpdateAttachments(req, frameworkAgreement.getId());
        int affected = frameworkAgreementMapper.updateById(frameworkAgreement);

        FrameworkAgreementProject frameworkAgreementProject = frameworkAgreementManager.getFrameworkAgreementProjectById(id);
        frameworkAgreementProjectConverter.update(req.getFrameworkAgreementProject(), frameworkAgreementProject);
        frameworkAgreementProject.setFrameworkAgreementId(id);
        frameworkAgreementProjectMapper.updateById(frameworkAgreementProject);

        FrameworkAgreementChannelEntry frameworkAgreementChannelEntry = frameworkAgreementManager.getFrameworkAgreementChannelEntryById(id);
        frameworkAgreementChannelEntryConverter.update(req.getFrameworkAgreementChannelEntry(), frameworkAgreementChannelEntry);
        frameworkAgreementChannelEntry.setFrameworkAgreementId(id);
        frameworkAgreementManager.compareAndUpdateAttachments(req.getFrameworkAgreementChannelEntry(), frameworkAgreementChannelEntry.getId());
        frameworkAgreementChannelEntryMapper.updateById(frameworkAgreementChannelEntry);

        FrameworkAgreementProjectFunding frameworkAgreementProjectFunding = frameworkAgreementManager.getFrameworkAgreementProjectFundingById(id);
        frameworkAgreementProjectFundingConverter.update(req.getFrameworkAgreementProjectFunding(), frameworkAgreementProjectFunding);
        frameworkAgreementProjectFunding.setFrameworkAgreementId(id);
        frameworkAgreementManager.compareAndUpdateAttachments(req.getFrameworkAgreementProjectFunding(), frameworkAgreementProjectFunding.getId());
        frameworkAgreementProjectFundingMapper.updateById(frameworkAgreementProjectFunding);

        visitManager.logicDeleteAllEntitiesByRefId(id);
        visitManager.createMany(req.getFrameworkVisits(), id);

        instanceRoleLarkMemberManager.deleteInstanceRoleLarkMember(id);
        instanceRoleLarkMemberManager.insertInstanceRoleLarkMember(req, id);

        return affected;
    }

    @Override
    public int logicDeleteOne(@NonNull Long id) {
        attachmentManager.deleteByOwnerId(id);
        attachmentManager.deleteByOwnerId(frameworkAgreementManager.getChannelEntryByFrameworkAgreementId(id).getId());
        attachmentManager.deleteByOwnerId(frameworkAgreementManager.getProjectFundingByFrameworkAgreementId(id).getId());
        frameworkAgreementProjectMapper.logicDeleteEntityById(frameworkAgreementManager.getFrameworkAgreementProjectById(id).getId());
        frameworkAgreementProjectFundingMapper.logicDeleteEntityById(frameworkAgreementManager.getProjectFundingByFrameworkAgreementId(id).getId());
        frameworkAgreementChannelEntryMapper.logicDeleteEntityById(frameworkAgreementManager.getChannelEntryByFrameworkAgreementId(id).getId());
        taskManager.logicDeleteEntityById(id);
        approvalManager.logicDeleteAllEntitiesByRefId(id);
        visitManager.logicDeleteAllEntitiesByRefId(id);
        return frameworkAgreementMapper.logicDeleteEntityById(id);
    }

}
