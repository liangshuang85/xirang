package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.ProjectType;
import eco.ywhc.xr.common.constant.TaskType;
import eco.ywhc.xr.common.converter.ProjectConverter;
import eco.ywhc.xr.common.converter.ProjectInformationConverter;
import eco.ywhc.xr.common.event.ProjectCreatedEvent;
import eco.ywhc.xr.common.model.dto.req.ProjectReq;
import eco.ywhc.xr.common.model.dto.res.ProjectInformationRes;
import eco.ywhc.xr.common.model.dto.res.ProjectRes;
import eco.ywhc.xr.common.model.dto.res.TaskRes;
import eco.ywhc.xr.common.model.entity.Project;
import eco.ywhc.xr.common.model.entity.ProjectInformation;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.common.model.query.ProjectQuery;
import eco.ywhc.xr.core.manager.ProjectManager;
import eco.ywhc.xr.core.manager.TaskManager;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static eco.ywhc.xr.core.service.ProjectServiceImpl.CodeGenerator.generateCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final ProjectMapper projectMapper;

    private final ProjectInformationMapper projectInformationMapper;

    private final ProjectManager projectManager;

    private final TaskManager taskManager;

    private final ProjectConverter projectConverter;

    private final ProjectInformationConverter projectInformationConverter;

    public static class CodeGenerator {

        private static int number = 1;

        public static String generateCode() {
            int year = LocalDate.now().getYear();
            String code = "XM" + year + String.format("%03d", number);
            number++;
            return code;
        }

    }

    @Override
    public Long createOne(@NonNull ProjectReq req) {
        Project project = projectConverter.fromRequest(req);
        project.setCode(generateCode());
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

        var results = rows.convert(i -> {
            ProjectRes res = projectConverter.toResponse(i);

            ProjectInformation project = projectManager.getProjectInformationByProjectId(i.getId());
            ProjectInformationRes projectRes = projectInformationConverter.toResponse(project);
            res.setProjectInformation(projectRes);

            return res;
        });
        return PageableModelSet.from(results);
    }

    @Override
    public ProjectRes findOne(@NonNull Long id) {
        Project project = projectManager.mustFoundEntityById(id);
        ProjectRes projectRes = projectConverter.toResponse(project);

        ProjectInformation projectInformation = projectManager.getProjectInformationByProjectId(id);
        ProjectInformationRes projectInformationRes = projectInformationConverter.toResponse(projectInformation);
        projectRes.setProjectInformation(projectInformationRes);

        List<Task> tasks = taskManager.listTasksByRefId(id);
        List<TaskRes> taskResList = new ArrayList<>();
        // 遍历任务列表，更新每个任务的状态
        for (Task task : tasks) {
            if (task.getTaskGuid() == null) {
                continue;
            }
            TaskRes larkTask = taskManager.getLarkTask(task);
            taskResList.add(larkTask);
        }
        Map<TaskType, List<TaskRes>> tasksResMaps = taskResList.stream().collect(Collectors.groupingBy(TaskRes::getType));
        projectRes.setTaskResMaps(tasksResMaps);
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
