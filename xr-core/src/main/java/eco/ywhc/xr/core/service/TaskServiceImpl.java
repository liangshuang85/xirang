package eco.ywhc.xr.core.service;

import com.lark.oapi.Client;
import com.lark.oapi.service.task.v2.model.CreateTaskReq;
import com.lark.oapi.service.task.v2.model.CreateTaskResp;
import com.lark.oapi.service.task.v2.model.InputTask;
import com.lark.oapi.service.task.v2.model.Member;
import eco.ywhc.xr.common.constant.TaskStatusType;
import eco.ywhc.xr.common.constant.TaskTemplateRefType;
import eco.ywhc.xr.common.constant.TaskType;
import eco.ywhc.xr.common.converter.TaskConverter;
import eco.ywhc.xr.common.event.InstanceRoleLarkMemberInsertedEvent;
import eco.ywhc.xr.common.model.TaskListInfo;
import eco.ywhc.xr.common.model.dto.req.TaskListReq;
import eco.ywhc.xr.common.model.dto.res.DepartmentRes;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.common.model.entity.TaskTemplate;
import eco.ywhc.xr.core.manager.FrameworkAgreementManager;
import eco.ywhc.xr.core.manager.ProjectManager;
import eco.ywhc.xr.core.manager.TaskManager;
import eco.ywhc.xr.core.manager.lark.LarkDepartmentManager;
import eco.ywhc.xr.core.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.sugar.commons.exception.ConditionNotMetException;
import org.sugar.commons.exception.InternalErrorException;
import org.sugar.commons.exception.InvalidInputException;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final String baseUrl = "https://oa.ywhc.lingycloud.com/ui/projectManagement/detail?id=";

    private final Client client;

    private final TaskMapper taskMapper;

    private final TaskManager taskManager;

    private final FrameworkAgreementManager frameworkAgreementManager;

    private final LarkDepartmentManager larkDepartmentManager;

    private final ProjectManager projectManager;

    private final TaskConverter taskConverter;

    @Override
    public int startLarkTask(long id) {
        Task currentTask = taskManager.findEntityById(id);
        if (currentTask.getStatus() == TaskStatusType.todo) {
            throw new InvalidInputException("当前任务未完成");
        }
        TaskTemplate taskTemplate = taskManager.getTaskTemplateById(currentTask.getTaskTemplateId());
        String summary;
        String name;
        if (taskTemplate.getRefType() == TaskTemplateRefType.FRAMEWORK_AGREEMENT) {
            name = frameworkAgreementManager.findEntityById(currentTask.getRefId()).getName();
            if (currentTask.getType() == TaskType.FRAMEWORK_AGREEMENT_PREPARATION) {
                summary = "【框架项目任务】" + "【项目建议书拟定】" + name;
            } else {
                summary = "【框架项目任务】" + "【框架协议拟定】" + name;
            }
        } else {
            name = projectManager.findEntityById(currentTask.getRefId()).getName();
            summary = "【项目管理任务】" + "【投资协议拟定】" + name;
        }
        // 获取任务清单
        String taskListGuid = taskManager.getTaskList(taskTemplate.getRefType(),name, currentTask.getRefId()).getTaskListGuid();
        // 获取任务分组
        String sectionGuid = taskManager.getSections(taskTemplate.getType(),taskListGuid);
        // 获取部门信息
        DepartmentRes department = larkDepartmentManager.getDepartmentByDepartmentId(currentTask.getDepartmentId());
        if (StringUtils.isBlank(department.getLeaderUserId())) {
            throw new ConditionNotMetException("部分负责人未设置，无法发起任务");
        }

        String description = "此任务为息壤机器人向" + department.getName() + "发起的自动任务请求，详情请见" + baseUrl + currentTask.getId() + "&edit=1";

        CreateTaskReq req = CreateTaskReq.newBuilder()
                .userIdType("open_id")
                .inputTask(InputTask.newBuilder()
                        .summary(summary)
                        .description(description)
                        .members(new Member[]{
                                Member.newBuilder()
                                        .id(department.getLeaderUserId())
                                        .type("user")
                                        .role("assignee")
                                        .build()
                        })
                        .build())
                .build();
        CreateTaskResp resp;
        try {
            resp = client.task().v2().task().create(req);
            TaskListReq taskListReq = TaskListReq.builder()
                    .taskGuid(resp.getData().getTask().getGuid())
                    .taskListGuid(taskListGuid)
                    .sectionGuid(sectionGuid)
                    .build();
            // 添加任务到任务清单
            taskManager.addTaskToTaskList(taskListReq);
            // 添加成员到任务清单
            taskManager.addMemberToTaskList(taskListGuid, Collections.singletonList(department.getLeaderUserId()));
        } catch (Exception e) {
            throw new InternalErrorException("任务发起失败");
        }
        if (!resp.success()) {
            throw new InternalErrorException("任务发起失败");
        }

        if (currentTask.getStatus() == TaskStatusType.pending) {
            currentTask.setTaskGuid(resp.getData().getTask().getGuid());
            currentTask.setStatus(TaskStatusType.todo);
            taskMapper.updateById(currentTask);
            return 1;
        } else if (currentTask.getStatus() == TaskStatusType.done || currentTask.getStatus() == TaskStatusType.deleted) {
            Task newTask = taskConverter.with(currentTask);
            newTask.setTaskGuid(resp.getData().getTask().getGuid());
            newTask.setStatus(TaskStatusType.todo);
            taskMapper.insert(newTask);
            return 1;
        }
        return 0;
    }

    @Override
    @TransactionalEventListener
    public void InstanceRoleLarkMemberInsertedEvent(InstanceRoleLarkMemberInsertedEvent event) {
        HandleInstanceRoleLarkMemberInsertedEvent(event);
    }

    private void HandleInstanceRoleLarkMemberInsertedEvent(InstanceRoleLarkMemberInsertedEvent event) {
        // 获取任务清单
        TaskListInfo taskList = taskManager.getTaskList(event.getRefType(), event.getName(), event.getId());
        // 查找需要移出任务清单的成员
        List<String> removeMembers = taskList.getMembers().stream()
                .filter(member -> !event.getCurrentMembers().contains(member))
                        .toList();
        // 添加成员到任务清单
        if(!event.getCurrentMembers().isEmpty()){
            taskManager.addMemberToTaskList(taskList.getTaskListGuid(), event.getCurrentMembers());
        }
        // 移出成员
        if(!removeMembers.isEmpty()){
        taskManager.removeMemberFromTaskList(taskList.getTaskListGuid(), removeMembers);
        }
    }

}
