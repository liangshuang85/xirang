package eco.ywhc.xr.core.service;

import com.lark.oapi.Client;
import com.lark.oapi.service.task.v2.model.CreateTaskReq;
import com.lark.oapi.service.task.v2.model.CreateTaskResp;
import com.lark.oapi.service.task.v2.model.InputTask;
import com.lark.oapi.service.task.v2.model.Member;
import eco.ywhc.xr.common.constant.TaskStatusType;
import eco.ywhc.xr.common.converter.TaskConverter;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.core.manager.TaskManager;
import eco.ywhc.xr.core.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;
import org.sugar.commons.exception.InvalidInputException;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final Client client;

    private final TaskMapper taskMapper;

    private final TaskManager taskManager;

    private final TaskConverter taskConverter;

    @Override
    public int startLarkTask(long id) {
        Task currentTask = taskManager.findEntityById(id);
        if (currentTask.getStatus() == TaskStatusType.todo) {
            throw new InvalidInputException("当前任务未完成");
        }

        CreateTaskReq req = CreateTaskReq.newBuilder()
                .userIdType("open_id")
                .inputTask(InputTask.newBuilder()
                        .summary(currentTask.getDepartment() + "任务")
                        .members(new Member[]{
                                Member.newBuilder()
                                        .id(currentTask.getAssigneeId())
                                        .type("user")
                                        .role("assignee")
                                        .build()
                        })
                        .build())
                .build();
        CreateTaskResp resp;
        try {
            resp = client.task().v2().task().create(req);
            if (!resp.success()) {
                throw new InternalErrorException("任务发起失败");
            }
        } catch (Exception e) {
            throw new InternalErrorException("任务发起失败");
        }

        if (currentTask.getStatus() == TaskStatusType.pending) {
            currentTask.setTaskGuid(resp.getData().getTask().getGuid());
            currentTask.setStatus(TaskStatusType.todo);
            taskMapper.updateById(currentTask);
            return 1;
        } else if (currentTask.getStatus() == TaskStatusType.done) {
            Task newTask = taskConverter.with(currentTask);
            newTask.setTaskGuid(resp.getData().getTask().getGuid());
            newTask.setStatus(TaskStatusType.todo);
            taskMapper.insert(newTask);
            return 1;
        }
        return 0;
    }

}
