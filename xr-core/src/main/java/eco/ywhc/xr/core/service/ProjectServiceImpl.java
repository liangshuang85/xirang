package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.*;
import eco.ywhc.xr.common.converter.ProjectConverter;
import eco.ywhc.xr.common.converter.ProjectInformationConverter;
import eco.ywhc.xr.common.converter.TaskConverter;
import eco.ywhc.xr.common.event.InstanceRoleLarkMemberInsertedEvent;
import eco.ywhc.xr.common.event.ProjectCreatedEvent;
import eco.ywhc.xr.common.event.ProjectUpdatedEvent;
import eco.ywhc.xr.common.event.StatusChangedEvent;
import eco.ywhc.xr.common.model.dto.req.ProjectReq;
import eco.ywhc.xr.common.model.dto.res.*;
import eco.ywhc.xr.common.model.entity.InstanceRole;
import eco.ywhc.xr.common.model.entity.Project;
import eco.ywhc.xr.common.model.entity.ProjectInformation;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.common.model.query.ProjectQuery;
import eco.ywhc.xr.common.security.CurrentUser;
import eco.ywhc.xr.core.manager.*;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import eco.ywhc.xr.core.mapper.ProjectMapper;
import eco.ywhc.xr.core.util.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.UniqueViolationException;
import org.sugar.crud.model.PageableModelSet;

import java.time.OffsetDateTime;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final AdministrativeDivisionManager administrativeDivisionManager;

    private final ApprovalManager approvalManager;

    private final AttachmentManager attachmentManager;

    private final BasicDataManager basicDataManager;

    private final ChangeManager changeManager;

    private final InstanceRoleManager instanceRoleManager;

    private final InstanceRoleLarkMemberManager instanceRoleLarkMemberManager;

    private final LarkEmployeeManager larkEmployeeManager;

    private final ProjectConverter projectConverter;

    private final ProjectInformationConverter projectInformationConverter;

    private final ProjectMapper projectMapper;

    private final ProjectManager projectManager;

    private final TaskConverter taskConverter;

    private final TaskManager taskManager;

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
        validateRequest(req, null);
        Project project = projectConverter.fromRequest(req);
        project.setCode(generateUniqueId());
        projectMapper.insert(project);
        projectManager.compareAndUpdateAttachments(req, project.getId());
        // 创建项目信息
        projectManager.createProjectInformation(req.getProjectInformation(), project.getId());
        // 创建基础信息
        basicDataManager.createOne(req.getBasicData(), project.getId());
        // 创建拜访记录
        visitManager.createMany(req.getProjectVisits(), project.getId());

        applicationEventPublisher.publishEvent(ProjectCreatedEvent.of(project));
        Task anyTask = taskManager.findAnyTaskByRefId(project.getId());
        String tasklistGuid = "";
        if (anyTask != null) {
            tasklistGuid = anyTask.getTasklistGuid();
        }
        if (CollectionUtils.isNotEmpty(req.getInstanceRoleLarkMembers())) {
            instanceRoleLarkMemberManager.insertInstanceRoleLarkMember(req.getInstanceRoleLarkMembers(), project.getId(), InstanceRefType.PROJECT);
            List<String> memberIds = instanceRoleLarkMemberManager.getMemberIdsByRefId(project.getId());
            applicationEventPublisher.publishEvent(InstanceRoleLarkMemberInsertedEvent.of(memberIds, tasklistGuid));
        }

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
        CurrentUser currentUser = SessionUtils.currentUser();

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

        // 如果没有全局 PROJECT:VIEW 权限，则检查实例权限
        if (!SessionUtils.currentUserPermissionCodes().contains("PROJECT:VIEW")) {
            String existsStatement = "SELECT 1 FROM `s_instance_role_lark_member` WHERE `deleted`=0 " +
                    " AND `member_id`='" + currentUser.getLarkOpenId() + "' " +
                    " AND `ref_type`='PROJECT' " +
                    " AND `ref_id`=`b_project`.id";
            qw.exists(existsStatement);
        }

        var rows = projectMapper.selectPage(query.paging(true), qw);
        if (CollectionUtils.isEmpty(rows.getRecords())) {
            return PageableModelSet.from(query.paging());
        }

        Set<Long> adcodes = rows.getRecords().stream().map(Project::getAdcode).collect(Collectors.toSet());
        Map<Long, AdministrativeDivisionRes> administrativeDivisionMap = administrativeDivisionManager.findAllAsMapByAdcodesSurely(adcodes);

        List<Long> clueIds = rows.getRecords().stream().map(Project::getId).toList();
        Map<Long, Set<String>> permissionMap = instanceRoleManager.listPermissionCodesByRefIdsAndMemberId(clueIds, currentUser.getLarkOpenId());

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

            res.setPermissionCodes(permissionMap.getOrDefault(i.getId(), Collections.emptySet()));

            return res;
        });
        return PageableModelSet.from(results);
    }

    @Override
    public ProjectRes findOne(@NonNull Long id) {
        CurrentUser currentUser = SessionUtils.currentUser();

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

        List<InstanceRoleLarkMemberRes> instanceRoleLarkMembers = instanceRoleLarkMemberManager.findInstanceRoleLarkMemberByRefId(id);
        res.setInstanceRoleLarkMembers(instanceRoleLarkMembers);

        List<Task> tasks = taskManager.listTasksByRefId(id);
        List<TaskRes> taskResList = new ArrayList<>();
        //  从数据库中查询任务
        for (Task task : tasks) {
            InstanceRole instanceRole = instanceRoleManager.findEntityById(task.getInstanceRoleId());
            TaskRes taskRes = taskConverter.toResponse(task);
            Set<String> memberIds = new HashSet<>();
            // 如果任务状态为待发起，则显示实例角色成员
            if (task.getStatus() == TaskStatusType.pending) {
                memberIds = instanceRoleLarkMembers.stream()
                        .filter(i -> i.getInstanceRoleId().equals(task.getInstanceRoleId()))
                        .findFirst()
                        .map(InstanceRoleLarkMemberRes::getMemberIds)
                        .orElse(Collections.emptySet());
            } else if (StringUtils.isNotBlank(task.getMembers())) {
                memberIds = Arrays.stream(task.getMembers().split(","))
                        .collect(Collectors.toSet());
            }
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
            taskRes.setInstanceRoleName(instanceRole.getName());
            taskResList.add(taskRes);
        }
        Map<TaskType, Map<String, List<TaskRes>>> taskMap = taskResList.stream()
                .collect(Collectors.groupingBy(
                        TaskRes::getType,
                        Collectors.groupingBy(TaskRes::getInstanceRoleName)
                ));
        res.setTaskMap(taskMap);

        Map<ApprovalType, Map<String, List<ApprovalRes>>> approvalResMaps = approvalManager.listApprovalsByRefId(id).stream()
                .collect(Collectors.groupingBy(
                        ApprovalRes::getType,
                        Collectors.groupingBy(ApprovalRes::getDepartmentName)
                ));
        res.setApprovalMap(approvalResMaps);


        List<ChangeRes> changes = changeManager.findAllByRefId(id);
        List<ChangeRes> changeRes = changes.stream().peek(i -> i.setOperator(assignee)).toList();
        res.setChanges(changeRes);

        res.setPermissionCodes(instanceRoleManager.listPermissionCodesByRefIdAndMemberId(id, currentUser.getLarkOpenId()));

        return res;
    }

    @Override
    public int updateOne(@NonNull Long id, @NonNull ProjectReq req) {
        validateRequest(req, id);
        Project project = projectManager.mustFoundEntityById(id);
        // 获取任务清单Guid
        Task anyTask = taskManager.findAnyTaskByRefId(id);
        String tasklistGuid = "";
        if(anyTask != null){
            tasklistGuid = anyTask.getTasklistGuid();
        }
        // 修改名称同时修改任务清单名
        if (req.getName() != null && !req.getName().equals(project.getName())&&StringUtils.isNotBlank(tasklistGuid)) {
            taskManager.updateTaskListName(tasklistGuid, TaskTemplateRefType.PROJECT, req.getName());
        }
        OffsetDateTime updatedAt = project.getUpdatedAt();
        ProjectStatusType currentStatus = project.getStatus();
        projectConverter.update(req, project);
        projectManager.compareAndUpdateAttachments(req, project.getId());
        int affected = projectMapper.updateById(project);
        // 更新项目信息
        projectManager.updateProjectInformation(req.getProjectInformation(), id);
        // 更新基础数据
        basicDataManager.updateOne(req.getBasicData(), id);
        // 更新拜访记录
        visitManager.logicDeleteAllEntitiesByRefId(id);
        visitManager.createMany(req.getProjectVisits(), id);

        instanceRoleLarkMemberManager.deleteInstanceRoleLarkMember(id);
        if (CollectionUtils.isNotEmpty(req.getInstanceRoleLarkMembers())) {
            instanceRoleLarkMemberManager.insertInstanceRoleLarkMember(req.getInstanceRoleLarkMembers(), id, InstanceRefType.PROJECT);
        }
        if (CollectionUtils.isNotEmpty(req.getInstanceRoleLarkMembers())) {
            List<String> memberIds = instanceRoleLarkMemberManager.getMemberIdsByRefId(project.getId());
            applicationEventPublisher.publishEvent(InstanceRoleLarkMemberInsertedEvent.of(memberIds, tasklistGuid));
        }

        // 发布项目已更新事件
        applicationEventPublisher.publishEvent(ProjectUpdatedEvent.of(project));

        // 发布状态变更事件
        if (currentStatus != req.getStatus()) {
            StatusChangedEvent statusChangedEvent = StatusChangedEvent.builder()
                    .refId(project.getId())
                    .refType(InstanceRefType.PROJECT)
                    .before(currentStatus.name())
                    .after(req.getStatus().name())
                    .operatorId(project.getAssigneeId())
                    .lastModifiedAt(updatedAt)
                    .build();
            applicationEventPublisher.publishEvent(statusChangedEvent);
        }

        return affected;
    }

    @Override
    public int logicDeleteOne(@NonNull Long id) {
        // 逻辑删除项目信息
        projectManager.logicDeleteProjectInformation(id);
        // 逻辑删除任务
        taskManager.logicDeleteEntityById(id);
        // 逻辑删除附件
        attachmentManager.deleteByOwnerId(id);
        //逻辑删除变更记录
        changeManager.bulkDeleteByRefId(id);
        // 逻辑删除基础信息
        basicDataManager.deleteEntityByRefId(id);
        return projectMapper.logicDeleteEntityById(id);
    }

    private void validateRequest(ProjectReq req, Long id) {
        Project project = findEntityByName(req.getName());
        if (project != null && !project.getId().equals(id)) {
            throw new UniqueViolationException("项目名称已存在");
        }
    }

    private Project findEntityByName(String name) {
        QueryWrapper<Project> qw = new QueryWrapper<>();
        qw.lambda().eq(Project::getDeleted, 0)
                .eq(Project::getName, name);
        return projectMapper.selectOne(qw);
    }

}
