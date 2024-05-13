package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.ApprovalType;
import eco.ywhc.xr.common.constant.FileOwnerType;
import eco.ywhc.xr.common.constant.InstanceRefType;
import eco.ywhc.xr.common.constant.TaskType;
import eco.ywhc.xr.common.converter.BasicDataConverter;
import eco.ywhc.xr.common.converter.ProjectConverter;
import eco.ywhc.xr.common.converter.ProjectInformationConverter;
import eco.ywhc.xr.common.converter.TaskConverter;
import eco.ywhc.xr.common.event.ProjectCreatedEvent;
import eco.ywhc.xr.common.event.StatusChangedEvent;
import eco.ywhc.xr.common.model.dto.req.ProjectReq;
import eco.ywhc.xr.common.model.dto.res.*;
import eco.ywhc.xr.common.model.entity.BasicData;
import eco.ywhc.xr.common.model.entity.Project;
import eco.ywhc.xr.common.model.entity.ProjectInformation;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.common.model.query.ProjectQuery;
import eco.ywhc.xr.core.manager.*;
import eco.ywhc.xr.core.manager.lark.LarkDepartmentManager;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import eco.ywhc.xr.core.mapper.BasicDataMapper;
import eco.ywhc.xr.core.mapper.ProjectInformationMapper;
import eco.ywhc.xr.core.mapper.ProjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.crud.model.PageableModelSet;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final ProjectMapper projectMapper;

    private final ProjectInformationMapper projectInformationMapper;

    private final BasicDataMapper basicDataMapper;

    private final ProjectManager projectManager;

    private final LarkEmployeeManager larkEmployeeManager;

    private final AdministrativeDivisionManager administrativeDivisionManager;

    private final TaskManager taskManager;

    private final BasicDataManager basicDataManager;

    private final ProjectConverter projectConverter;

    private final ProjectInformationConverter projectInformationConverter;

    private final BasicDataConverter basicDataConverter;

    private final TaskConverter taskConverter;

    private final ApprovalManager approvalManager;

    private final AttachmentManager attachmentManager;

    private final LarkDepartmentManager larkDepartmentManager;

    private final InstanceRoleLarkMemberManager instanceRoleLarkMemberManager;

    private final ChangeManager changeManager;

    private final VisitManager visitManager;

    public String generateUniqueId() {
        QueryWrapper<Project> qw = new QueryWrapper<>();
        qw.select("id", "code").orderByDesc("id").last("LIMIT 1");
        var project = projectMapper.selectOne(qw);
        int num = 1;
        if (project != null) {
            String numString = project.getCode().substring(6);
            num = Integer.parseInt(numString) + 1;
        }
        String formattedNum = String.format("%03d", num);
        return "XM" + Year.now() + formattedNum;
    }

    @Override
    public Long createOne(@NonNull ProjectReq req) {
        Project project = projectConverter.fromRequest(req);
        project.setCode(generateUniqueId());
        projectMapper.insert(project);
        projectManager.compareAndUpdateAttachments(req, project.getId());
        ProjectInformation projectInformation = projectInformationConverter.fromRequest(req.getProjectInformation());
        projectInformation.setProjectId(project.getId());
        projectInformationMapper.insert(projectInformation);
        instanceRoleLarkMemberManager.insertInstanceRoleLarkMember(req, project.getId());
        // 创建基础信息
        BasicData basicData = basicDataConverter.fromRequest(req.getBasicData());
        basicData.setRefId(project.getId());
        basicDataMapper.insert(basicData);
        // 创建拜访记录
        visitManager.createMany(req.getProjectVisits(), project.getId());

        applicationEventPublisher.publishEvent(ProjectCreatedEvent.of(project));

        StatusChangedEvent statusChangedEvent = StatusChangedEvent.builder()
                .refId(project.getId())
                .refType(InstanceRefType.PROJECT)
                .before("")
                .after(project.getStatus().name())
                .operatorId(project.getAssigneeId())
                .lastModifiedAt(project.getCreatedAt())
                .build();
        applicationEventPublisher.publishEvent(statusChangedEvent);

        return project.getId();
    }

    @Override
    public PageableModelSet<ProjectRes> findMany(@NonNull ProjectQuery query) {
        List<Long> adIds = new ArrayList<>();
        if (query.getAdcode() != null) {
            adIds = administrativeDivisionManager.findAllEntityIdsSince(query.getAdcode());
            if (CollectionUtils.isEmpty(adIds)) {
                return PageableModelSet.from(query.paging());
            }
        }
        QueryWrapper<Project> qw = new QueryWrapper<>();
        qw.lambda().eq(Project::getDeleted, 0)
                .eq(StringUtils.isNotBlank(query.getAssigneeId()), Project::getAssigneeId, query.getAssigneeId())
                .eq(Objects.nonNull(query.getStatus()), Project::getStatus, query.getStatus())
                .in(CollectionUtils.isNotEmpty(adIds), Project::getAdcode, adIds)
                .orderByDesc(Project::getId);

        var rows = projectMapper.selectPage(query.paging(true), qw);
        if (CollectionUtils.isEmpty(rows.getRecords())) {
            return PageableModelSet.from(query.paging());
        }

        Set<Long> adcodes = rows.getRecords().stream().map(Project::getAdcode).collect(Collectors.toSet());
        Map<Long, AdministrativeDivisionRes> administrativeDivisionMap = administrativeDivisionManager.findAllAsMapByAdcodesSurely(adcodes);

        var results = rows.convert(i -> {
            ProjectRes res = projectConverter.toResponse(i);
            res.setAdministrativeDivision(administrativeDivisionMap.get(i.getAdcode()));
            LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployee(i.getAssigneeId());
            ProjectInformation project = projectManager.getProjectInformationByProjectId(i.getId());
            ProjectInformationRes projectRes = projectInformationConverter.toResponse(project);

            AssigneeRes projectAssignee = AssigneeRes.builder()
                    .assigneeId(i.getAssigneeId())
                    .assigneeName(larkEmployee.getName())
                    .avatarInfo(larkEmployee.getAvatarInfo())
                    .build();
            res.setAssignee(projectAssignee);
            res.setProjectInformation(projectRes);

            return res;
        });
        return PageableModelSet.from(results);
    }

    @Override
    public ProjectRes findOne(@NonNull Long id) {
        Project project = projectManager.mustFoundEntityById(id);
        ProjectRes projectRes = projectConverter.toResponse(project);
        List<AttachmentResponse> meetingResolutionAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.MEETING_RESOLUTION);
        projectRes.setMeetingResolutionAttachments(meetingResolutionAttachments);
        List<AttachmentResponse> meetingMinutesAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.MEETING_MINUTES);
        projectRes.setMeetingMinutesAttachments(meetingMinutesAttachments);
        List<AttachmentResponse> investmentAgreementAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.INVESTMENT_AGREEMENT);
        projectRes.setInvestmentAgreementAttachments(investmentAgreementAttachments);
        List<AttachmentResponse> investmentAgreementSigningAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.INVESTMENT_AGREEMENT_SIGNING);
        projectRes.setInvestmentAgreementSigningAttachments(investmentAgreementSigningAttachments);
        List<AttachmentResponse> enterpriseInvestmentRecordAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.ENTERPRISE_INVESTMENT_RECORD);
        projectRes.setEnterpriseInvestmentRecordAttachments(enterpriseInvestmentRecordAttachments);

        AdministrativeDivisionRes administrativeDivision = administrativeDivisionManager.findByAdcodeSurely(project.getAdcode());
        projectRes.setAdministrativeDivision(administrativeDivision);

        ProjectInformation projectInformation = projectManager.getProjectInformationByProjectId(id);
        ProjectInformationRes projectInformationRes = projectInformationConverter.toResponse(projectInformation);
        projectRes.setProjectInformation(projectInformationRes);

        // 获取基础信息
        projectRes.setBasicData(basicDataConverter.toResponse(basicDataManager.findEntityByRefId(id)));
        // 获取拜访记录
        List<VisitRes> visitList = visitManager.findAllByRefId(id);
        projectRes.setProjectVisits(visitList);

        LarkEmployee projectLarkEmployee = larkEmployeeManager.retrieveLarkEmployee(project.getAssigneeId());
        AssigneeRes assignee = AssigneeRes.builder()
                .assigneeId(project.getAssigneeId())
                .assigneeName(projectLarkEmployee.getName())
                .avatarInfo(projectLarkEmployee.getAvatarInfo())
                .build();
        projectRes.setAssignee(assignee);

        List<Task> tasks = taskManager.listTasksByRefId(id);
        List<TaskRes> taskResList = new ArrayList<>();
        // 遍历任务列表，更新每个任务的状态
        for (Task task : tasks) {
            if (task.getTaskGuid() == null) {
                TaskRes taskRes = taskConverter.toResponse(task);
                taskResList.add(taskRes);
                continue;
            }
            TaskRes larkTask = taskManager.getLarkTask(task);
            DepartmentRes department = larkDepartmentManager.getDepartmentByDepartmentId(task.getDepartmentId());
            String leaderUserId = department.getLeaderUserId();
            if (StringUtils.isNotBlank(leaderUserId)) {
                LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployee(leaderUserId);
                AssigneeRes taskAssignee = AssigneeRes.builder()
                        .assigneeId(leaderUserId)
                        .assigneeName(larkEmployee.getName())
                        .avatarInfo(larkEmployee.getAvatarInfo())
                        .build();
                larkTask.setAssignee(taskAssignee);
            }
            taskResList.add(larkTask);
        }
        Map<TaskType, Map<String, List<TaskRes>>> taskMap = taskResList.stream()
                .collect(Collectors.groupingBy(
                        TaskRes::getType,
                        Collectors.groupingBy(TaskRes::getDepartmentName)
                ));
        projectRes.setTaskMap(taskMap);

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
        projectRes.setApprovalMap(approvalResMaps);

        List<InstanceRoleLarkMemberRes> instanceRoleLarkMemberRes = instanceRoleLarkMemberManager.findInstanceRoleLarkMemberByRefId(id);
        projectRes.setInstanceRoleLarkMembers(instanceRoleLarkMemberRes);

        List<ChangeRes> changes = changeManager.findAllByRefId(id);
        List<ChangeRes> changeRes = changes.stream().peek(i -> i.setOperator(assignee)).toList();
        projectRes.setChanges(changeRes);

        return projectRes;
    }

    @Override
    public int updateOne(@NonNull Long id, @NonNull ProjectReq req) {
        Project project = projectManager.mustFoundEntityById(id);
        projectConverter.update(req, project);
        projectManager.compareAndUpdateAttachments(req, project.getId());
        int affected = projectMapper.updateById(project);

        ProjectInformation projectInformation = projectManager.getProjectInformationByProjectId(id);
        projectInformationConverter.update(req.getProjectInformation(), projectInformation);
        projectInformation.setProjectId(id);
        projectInformationMapper.updateById(projectInformation);

        // 更新基础数据
        BasicData basicData = basicDataManager.findEntityByRefId(id);
        basicDataConverter.update(req.getBasicData(), basicData);
        basicData.setRefId(id);
        basicDataMapper.updateById(basicData);
        // 更新拜访记录
        visitManager.logicDeleteAllEntitiesByRefId(id);
        visitManager.createMany(req.getProjectVisits(), id);

        instanceRoleLarkMemberManager.deleteInstanceRoleLarkMember(id);
        instanceRoleLarkMemberManager.insertInstanceRoleLarkMember(req, id);

        if (project.getStatus() != req.getStatus()) {
            StatusChangedEvent statusChangedEvent = StatusChangedEvent.builder()
                    .refId(project.getId())
                    .refType(InstanceRefType.PROJECT)
                    .before(project.getStatus().name())
                    .after(req.getStatus().name())
                    .operatorId(project.getAssigneeId())
                    .lastModifiedAt(project.getUpdatedAt())
                    .build();
            applicationEventPublisher.publishEvent(statusChangedEvent);
        }

        return affected;
    }

    @Override
    public int logicDeleteOne(@NonNull Long id) {
        projectInformationMapper.logicDeleteEntityById(projectManager.getProjectInformationByProjectId(id).getId());
        taskManager.logicDeleteEntityById(id);
        attachmentManager.deleteByOwnerId(id);
        changeManager.bulkDeleteByRefId(id);
        // 逻辑删除基础信息
        basicDataManager.deleteEntityByRefId(id);
        return projectMapper.logicDeleteEntityById(id);
    }

}
