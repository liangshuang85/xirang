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
        frameworkAgreementManager.linkAttachments(req, frameworkAgreement.getId());

        FrameworkAgreementChannelEntry frameworkAgreementChannelEntry = frameworkAgreementChannelEntryConverter.fromRequest(req.getFrameworkAgreementChannelEntry());
        frameworkAgreementChannelEntry.setFrameworkAgreementId(frameworkAgreement.getId());
        frameworkAgreementChannelEntryMapper.insert(frameworkAgreementChannelEntry);
        frameworkAgreementManager.linkAttachments(req.getFrameworkAgreementChannelEntry(), frameworkAgreementChannelEntry.getId());

        FrameworkAgreementProjectFunding frameworkAgreementProjectFunding = frameworkAgreementProjectFundingConverter.fromRequest(req.getFrameworkAgreementProjectFunding());
        frameworkAgreementProjectFunding.setFrameworkAgreementId(frameworkAgreement.getId());
        frameworkAgreementProjectFundingMapper.insert(frameworkAgreementProjectFunding);
        frameworkAgreementManager.linkAttachments(req.getFrameworkAgreementProjectFunding(), frameworkAgreementProjectFunding.getId());

        FrameworkAgreementProject frameworkAgreementProject = frameworkAgreementProjectConverter.fromRequest(req.getFrameworkAgreementProject());
        frameworkAgreementProject.setFrameworkAgreementId(frameworkAgreement.getId());
        frameworkAgreementProjectMapper.insert(frameworkAgreementProject);

        visitManager.createMany(req.getFrameworkVisits(), frameworkAgreement.getId());

        applicationEventPublisher.publishEvent(FrameworkAgreementCreatedEvent.of(frameworkAgreement));

        return frameworkAgreement.getId();
    }

    @Override
    public PageableModelSet<FrameworkAgreementRes> findMany(@NonNull FrameworkAgreementQuery query) {
        QueryWrapper<FrameworkAgreement> qw = new QueryWrapper<>();
        qw.lambda().eq(FrameworkAgreement::getDeleted, 0)
                .eq(StringUtils.isNotBlank(query.getAssigneeId()), FrameworkAgreement::getAssigneeId, query.getAssigneeId())
                .eq(Objects.nonNull(query.getAdcode()), FrameworkAgreement::getAdcode, query.getAdcode())
                .eq(Objects.nonNull(query.getStatus()), FrameworkAgreement::getStatus, query.getStatus());

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
        //查询框架协议关联的附件
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
        // 遍历任务列表，更新每个任务的状态
        for (Task task : tasks) {
            if (task.getTaskGuid() == null) {
                TaskRes taskRes = taskConverter.toResponse(task);
                taskResList.add(taskRes);
                continue;
            }
            try {
                TaskRes larkTask = taskManager.getLarkTask(task);
                LarkEmployee taskLarkEmployee = larkEmployeeManager.retrieveLarkEmployee(task.getAssigneeId());
                AssigneeRes taskAssignee = AssigneeRes.builder()
                        .assigneeId(task.getAssigneeId())
                        .assigneeName(taskLarkEmployee.getName())
                        .avatarInfo(taskLarkEmployee.getAvatarInfo())
                        .build();
                larkTask.setAssignee(taskAssignee);
                taskResList.add(larkTask);
            } catch (Exception e) {
                throw new InternalErrorException("查询飞书任务失败");
            }
        }
        Map<TaskType, List<TaskRes>> taskMap = taskResList.stream().collect(Collectors.groupingBy(TaskRes::getType));
        res.setTaskMap(taskMap);

        Map<ApprovalType, List<ApprovalRes>> approvalResMaps = approvalManager.listApprovalsByRefId(id).stream()
                .peek(i -> {
                    if (i.getApprovalInstanceId() != null) {
                        approvalManager.updateApproval(i);
                    }
                })
                .collect(Collectors.groupingBy(ApprovalRes::getType));
        res.setApprovalMap(approvalResMaps);

        List<VisitRes> visitList = visitManager.findAllByRefId(id);
        res.setFrameworkVisits(visitList);

        return res;
    }

    @Override
    public int updateOne(@NonNull Long id, @NonNull FrameworkAgreementReq req) {
        FrameworkAgreement frameworkAgreement = frameworkAgreementManager.mustFoundEntityById(id);
        frameworkAgreementConverter.update(req, frameworkAgreement);
        frameworkAgreementManager.linkAttachments(req, frameworkAgreement.getId());
        int affected = frameworkAgreementMapper.updateById(frameworkAgreement);

        if (req.getFrameworkAgreementProject() != null) {
            frameworkAgreementProjectMapper.logicDeleteEntityById(frameworkAgreementManager.getProjectByFrameworkAgreementId(id).getId());
            FrameworkAgreementProject frameworkAgreementProject = frameworkAgreementProjectConverter.fromRequest(req.getFrameworkAgreementProject());
            frameworkAgreementProject.setFrameworkAgreementId(frameworkAgreement.getId());
            frameworkAgreementProjectMapper.insert(frameworkAgreementProject);
        }

        if (req.getFrameworkAgreementChannelEntry() != null) {
            frameworkAgreementChannelEntryMapper.logicDeleteEntityById(frameworkAgreementManager.getChannelEntryByFrameworkAgreementId(id).getId());
            FrameworkAgreementChannelEntry frameworkAgreementChannelEntry = frameworkAgreementChannelEntryConverter.fromRequest(req.getFrameworkAgreementChannelEntry());
            frameworkAgreementChannelEntry.setFrameworkAgreementId(frameworkAgreement.getId());
            frameworkAgreementChannelEntryMapper.insert(frameworkAgreementChannelEntry);
            frameworkAgreementManager.linkAttachments(req.getFrameworkAgreementChannelEntry(), frameworkAgreementChannelEntry.getId());
        }

        if (req.getFrameworkAgreementProjectFunding() != null) {
            frameworkAgreementProjectFundingMapper.logicDeleteEntityById(frameworkAgreementManager.getProjectFundingByFrameworkAgreementId(id).getId());
            FrameworkAgreementProjectFunding frameworkAgreementProjectFunding = frameworkAgreementProjectFundingConverter.fromRequest(req.getFrameworkAgreementProjectFunding());
            frameworkAgreementProjectFunding.setFrameworkAgreementId(frameworkAgreement.getId());
            frameworkAgreementProjectFundingMapper.insert(frameworkAgreementProjectFunding);
            frameworkAgreementManager.linkAttachments(req.getFrameworkAgreementProjectFunding(), frameworkAgreementProjectFunding.getId());
        }

        return affected;
    }

    @Override
    public int logicDeleteOne(@NonNull Long id) {
        frameworkAgreementProjectMapper.logicDeleteEntityById(frameworkAgreementManager.getProjectByFrameworkAgreementId(id).getId());
        frameworkAgreementProjectFundingMapper.logicDeleteEntityById(frameworkAgreementManager.getProjectFundingByFrameworkAgreementId(id).getId());
        frameworkAgreementChannelEntryMapper.logicDeleteEntityById(frameworkAgreementManager.getChannelEntryByFrameworkAgreementId(id).getId());
        taskManager.logicDeleteEntityById(id);
        return frameworkAgreementMapper.logicDeleteEntityById(id);
    }

}
