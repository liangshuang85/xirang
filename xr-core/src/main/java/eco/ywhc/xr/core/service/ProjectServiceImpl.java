package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.ApprovalType;
import eco.ywhc.xr.common.constant.ProjectType;
import eco.ywhc.xr.common.constant.TaskType;
import eco.ywhc.xr.common.converter.ProjectConverter;
import eco.ywhc.xr.common.converter.ProjectInformationConverter;
import eco.ywhc.xr.common.converter.TaskConverter;
import eco.ywhc.xr.common.event.ProjectCreatedEvent;
import eco.ywhc.xr.common.model.dto.req.ProjectReq;
import eco.ywhc.xr.common.model.dto.res.*;
import eco.ywhc.xr.common.model.entity.Project;
import eco.ywhc.xr.common.model.entity.ProjectInformation;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.common.model.query.ProjectQuery;
import eco.ywhc.xr.core.manager.AdministrativeDivisionManager;
import eco.ywhc.xr.core.manager.ApprovalManager;
import eco.ywhc.xr.core.manager.ProjectManager;
import eco.ywhc.xr.core.manager.TaskManager;
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

    private final ProjectConverter projectConverter;

    private final ProjectInformationConverter projectInformationConverter;

    private final TaskConverter taskConverter;

    private final ApprovalManager approvalManager;


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
        project.setStatus(ProjectType.PENDING_PROJECT_MEETING);
        projectMapper.insert(project);

        ProjectInformation projectInformation = projectInformationConverter.fromRequest(req.getProjectInformation());
        projectInformation.setProjectId(project.getId());
        projectInformationMapper.insert(projectInformation);

        applicationEventPublisher.publishEvent(ProjectCreatedEvent.of(project));

        return project.getId();
    }

    @Override
    public PageableModelSet<ProjectRes> findMany(@NonNull ProjectQuery query) {
        QueryWrapper<Project> qw = new QueryWrapper<>();
        qw.lambda().eq(Project::getDeleted, 0)
                .eq(StringUtils.isNotBlank(query.getAssigneeId()), Project::getAssigneeId, query.getAssigneeId())
                .eq(Objects.nonNull(query.getAdcode()), Project::getAdcode, query.getAdcode())
                .eq(Objects.nonNull(query.getStatus()), Project::getStatus, query.getStatus());

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

        AdministrativeDivisionRes administrativeDivision = administrativeDivisionManager.findByAdcodeSurely(project.getAdcode());
        projectRes.setAdministrativeDivision(administrativeDivision);

        ProjectInformation projectInformation = projectManager.getProjectInformationByProjectId(id);
        ProjectInformationRes projectInformationRes = projectInformationConverter.toResponse(projectInformation);
        projectRes.setProjectInformation(projectInformationRes);

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
            LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployee(task.getAssigneeId());
            AssigneeRes taskAssignee = AssigneeRes.builder()
                    .assigneeId(task.getAssigneeId())
                    .assigneeName(larkEmployee.getName())
                    .avatarInfo(larkEmployee.getAvatarInfo())
                    .build();
            larkTask.setAssignee(taskAssignee);
            taskResList.add(larkTask);
        }
        Map<TaskType, List<TaskRes>> taskMap = taskResList.stream().collect(Collectors.groupingBy(TaskRes::getType));
        projectRes.setTaskMap(taskMap);

        Map<ApprovalType, List<ApprovalRes>> approvalResMaps = approvalManager.listApprovalsByRefId(id).stream()
                .peek(i -> {
                    if (i.getApprovalInstanceId() != null) {
                        approvalManager.updateApproval(i);
                    }
                })
                .collect(Collectors.groupingBy(ApprovalRes::getType));
        projectRes.setApprovalMap(approvalResMaps);

        return projectRes;
    }

    @Override
    public int updateOne(@NonNull Long id, @NonNull ProjectReq req) {
        Project project = projectManager.mustFoundEntityById(id);
        projectConverter.update(req, project);
        int affected = projectMapper.updateById(project);

        if (req.getProjectInformation() != null) {
            projectInformationMapper.logicDeleteEntityById(projectManager.getProjectInformationByProjectId(id).getId());
            ProjectInformation projectInformation = projectInformationConverter.fromRequest(req.getProjectInformation());
            projectInformation.setProjectId(project.getId());
            projectInformationMapper.insert(projectInformation);
        }
        return affected;
    }

    @Override
    public int logicDeleteOne(@NonNull Long id) {
        projectInformationMapper.logicDeleteEntityById(projectManager.getProjectInformationByProjectId(id).getId());
        taskManager.logicDeleteEntityById(id);
        return projectMapper.logicDeleteEntityById(id);
    }

}
