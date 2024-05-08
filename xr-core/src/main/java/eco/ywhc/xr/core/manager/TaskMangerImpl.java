package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lark.oapi.Client;
import com.lark.oapi.service.task.v2.model.GetTaskReq;
import com.lark.oapi.service.task.v2.model.GetTaskResp;
import eco.ywhc.xr.common.constant.TaskStatusType;
import eco.ywhc.xr.common.model.dto.res.TaskRes;
import eco.ywhc.xr.common.model.entity.BaseEntity;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.common.model.entity.TaskTemplate;
import eco.ywhc.xr.core.mapper.TaskMapper;
import eco.ywhc.xr.core.mapper.TaskTemplateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskMangerImpl implements TaskManager {

    private final TaskMapper taskMapper;

    private final TaskTemplateMapper taskTemplateMapper;

    private final Client client;

    @Override
    public List<Task> listTasksByRefId(long id) {
        QueryWrapper<Task> qw = new QueryWrapper<>();
        qw.lambda().eq(BaseEntity::getDeleted, 0)
                .eq(Task::getRefId, id)
                .orderByDesc(Task::getId);
        return taskMapper.selectList(qw);
    }

    @Override
    public void logicDeleteEntityById(long refId) {
        QueryWrapper<Task> qw = new QueryWrapper<>();
        qw.lambda().eq(Task::getDeleted, 0)
                .eq(Task::getRefId, refId);
        taskMapper.selectList(qw).stream()
                .map(Task::getId)
                .forEach(taskMapper::logicDeleteEntityById);
    }

    @Override
    public TaskRes getLarkTask(Task task) {
        GetTaskReq req = GetTaskReq.newBuilder()
                .taskGuid(task.getTaskGuid())
                .build();
        GetTaskResp resp;
        try {
            resp = client.task().v2().task().get(req);
            if (!resp.success()) {
                throw new InternalErrorException();
            }
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }

        // 业务数据处理
        var taskData = resp.getData().getTask();
        task.setCompletedAt(taskData.getCompletedAt());
        taskMapper.updateById(task);

        TaskRes res = new TaskRes();
        if (!Objects.equals(task.getCompletedAt(), "0")) {
            task.setStatus(TaskStatusType.done);
            taskMapper.updateById(task);
            res.setStatus(TaskStatusType.done);
        } else {
            res.setStatus(task.getStatus());
        }
        res.setDepartmentName(task.getDepartment());
        res.setId(task.getId());
        res.setTaskUrl(taskData.getUrl());
        res.setSummary(taskData.getSummary());
        res.setCompletedAt(taskData.getCompletedAt());
        res.setType(task.getType());
        return res;
    }

    @Override
    public TaskTemplate getTaskTemplateById(long id) {
        QueryWrapper<TaskTemplate> qw = new QueryWrapper<>();
        qw.lambda().eq(TaskTemplate::getDeleted, 0)
                .eq(TaskTemplate::getId, id);
        return taskTemplateMapper.selectOne(qw);
    }

    @Override
    public List<Task> listTasks(long prevTaskId, int limit) {
        QueryWrapper<Task> qw = new QueryWrapper<>();
        qw.lambda().eq(Task::getDeleted, 0)
                .eq(Task::getStatus, TaskStatusType.todo)
                .gt(Task::getId, prevTaskId)
                .orderByAsc(Task::getId)
                .last("LIMIT " + limit);
        return taskMapper.selectList(qw);
    }

    @Override
    public Task findEntityById(@NonNull Long id) {
        return taskMapper.findEntityById(id);
    }

}
