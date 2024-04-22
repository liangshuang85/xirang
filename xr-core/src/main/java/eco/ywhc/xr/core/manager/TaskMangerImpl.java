package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lark.oapi.Client;
import com.lark.oapi.service.task.v2.model.GetTaskReq;
import com.lark.oapi.service.task.v2.model.GetTaskResp;
import eco.ywhc.xr.common.constant.TaskStatusType;
import eco.ywhc.xr.common.model.dto.res.TaskRes;
import eco.ywhc.xr.common.model.entity.BaseEntity;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.core.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskMangerImpl implements TaskManager {

    private final TaskMapper taskMapper;

    private final Client client;

    @Override
    public List<Task> listTasksByRefId(long id) {
        QueryWrapper<Task> qw = new QueryWrapper<>();
        qw.lambda().eq(BaseEntity::getDeleted, 0)
                .eq(Task::getRefId, id);
        List<Task> tasks = taskMapper.selectList(qw);
        return tasks.stream()
                .collect(Collectors.toMap(
                        Task::getTaskTemplateId,
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparing(Task::getCreatedAt))
                ))
                .values()
                .stream()
                .toList();
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
        res.setId(task.getId());
        res.setTaskUrl(taskData.getUrl());
        res.setSummary(taskData.getSummary());
        res.setCompletedAt(taskData.getCompletedAt());
        res.setType(task.getType());
        return res;
    }

    @Override
    public Task findEntityById(@NonNull Long id) {
        return taskMapper.findEntityById(id);
    }

}
