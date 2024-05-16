package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.*;
import eco.ywhc.xr.common.converter.ProjectConverter;
import eco.ywhc.xr.common.converter.ProjectInformationConverter;
import eco.ywhc.xr.common.converter.TaskConverter;
import eco.ywhc.xr.common.event.InstanceRoleLarkMemberInsertedEvent;
import eco.ywhc.xr.common.event.ProjectCreatedEvent;
import eco.ywhc.xr.common.event.StatusChangedEvent;
import eco.ywhc.xr.common.model.dto.req.ProjectReq;
import eco.ywhc.xr.common.model.dto.res.*;
import eco.ywhc.xr.common.model.entity.Project;
import eco.ywhc.xr.common.model.entity.ProjectInformation;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.common.model.query.ProjectQuery;
import eco.ywhc.xr.core.manager.*;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
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

    private final ProjectManager projectManager;

    private final LarkEmployeeManager larkEmployeeManager;

    private final AdministrativeDivisionManager administrativeDivisionManager;

    private final TaskManager taskManager;

    private final BasicDataManager basicDataManager;

    private final ProjectConverter projectConverter;

    private final ProjectInformationConverter projectInformationConverter;

    private final TaskConverter taskConverter;

    private final ApprovalManager approvalManager;

    private final AttachmentManager attachmentManager;

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
        basicDataManager.createOne(req.getBasicData(), project.getId());
        // 创建拜访记录
        visitManager.createMany(req.getProjectVisits(), project.getId());

        if (CollectionUtils.isNotEmpty(req.getInstanceRoleLarkMembers())) {
            instanceRoleLarkMemberManager.insertInstanceRoleLarkMember(req, project.getId());
            List<String> memberIds = instanceRoleLarkMemberManager.getMemberIdsByRefId(project.getId());
            applicationEventPublisher.publishEvent(InstanceRoleLarkMemberInsertedEvent.of(project.getId(), req.getName(), TaskTemplateRefType.PROJECT, memberIds));
        }

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
        ProjectRes res = projectConverter.toResponse(project);
        projectManager.findAndSetAttachments(res);

        AdministrativeDivisionRes administrativeDivision = administrativeDivisionManager.findByAdcodeSurely(project.getAdcode());
        res.setAdministrativeDivision(administrativeDivision);

        ProjectInformation projectInformation = projectManager.getProjectInformationByProjectId(id);
        ProjectInformationRes projectInformationRes = projectInformationConverter.toResponse(projectInformation);
        res.setProjectInformation(projectInformationRes);

        // 获取基础信息
        res.setBasicData(basicDataManager.getBasicData(id));
        // 获取拜访记录
        List<VisitRes> visitList = visitManager.findAllByRefId(id);
        res.setProjectVisits(visitList);

        LarkEmployee projectLarkEmployee = larkEmployeeManager.retrieveLarkEmployee(project.getAssigneeId());
        AssigneeRes assignee = AssigneeRes.builder()
                .assigneeId(project.getAssigneeId())
                .assigneeName(projectLarkEmployee.getName())
                .avatarInfo(projectLarkEmployee.getAvatarInfo())
                .build();
        res.setAssignee(assignee);

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

        List<InstanceRoleLarkMemberRes> instanceRoleLarkMemberRes = instanceRoleLarkMemberManager.findInstanceRoleLarkMemberByRefId(id);
        res.setInstanceRoleLarkMembers(instanceRoleLarkMemberRes);

        List<ChangeRes> changes = changeManager.findAllByRefId(id);
        List<ChangeRes> changeRes = changes.stream().peek(i -> i.setOperator(assignee)).toList();
        res.setChanges(changeRes);

        return res;
    }

    @Override
    public int updateOne(@NonNull Long id, @NonNull ProjectReq req) {
        Project project = projectManager.mustFoundEntityById(id);
        ProjectStatusType currentStatus = project.getStatus();
        projectConverter.update(req, project);
        projectManager.compareAndUpdateAttachments(req, project.getId());
        int affected = projectMapper.updateById(project);

        ProjectInformation projectInformation = projectManager.getProjectInformationByProjectId(id);
        projectInformationConverter.update(req.getProjectInformation(), projectInformation);
        projectInformation.setProjectId(id);
        projectInformationMapper.updateById(projectInformation);

        // 更新基础数据
        basicDataManager.updateOne(req.getBasicData(), id);
        // 更新拜访记录
        visitManager.logicDeleteAllEntitiesByRefId(id);
        visitManager.createMany(req.getProjectVisits(), id);

        instanceRoleLarkMemberManager.deleteInstanceRoleLarkMember(id);
        if (CollectionUtils.isNotEmpty(req.getInstanceRoleLarkMembers())) {
            instanceRoleLarkMemberManager.insertInstanceRoleLarkMember(req, id);
        }
        if (CollectionUtils.isNotEmpty(req.getInstanceRoleLarkMembers())) {
            List<String> memberIds = instanceRoleLarkMemberManager.getMemberIdsByRefId(project.getId());
            applicationEventPublisher.publishEvent(InstanceRoleLarkMemberInsertedEvent.of(id, req.getName(), TaskTemplateRefType.PROJECT, memberIds));
        }

        // 发布状态变更事件
        if (currentStatus != req.getStatus()) {
            StatusChangedEvent statusChangedEvent = StatusChangedEvent.builder()
                    .refId(project.getId())
                    .refType(InstanceRefType.PROJECT)
                    .before(currentStatus.name())
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
