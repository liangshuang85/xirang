package eco.ywhc.xr.core.event;

import eco.ywhc.xr.common.constant.TaskStatusType;
import eco.ywhc.xr.common.constant.TaskTemplateRefType;
import eco.ywhc.xr.common.constant.TaskType;
import eco.ywhc.xr.common.event.ProjectCreatedEvent;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.common.model.entity.TaskTemplate;
import eco.ywhc.xr.core.manager.TaskTemplateManager;
import eco.ywhc.xr.core.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

/**
 * 项目相关的事件
 */
@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class ProjectEventListener {

    private final TaskTemplateManager taskTemplateManager;

    private final TaskMapper taskMapper;

    @Async
    @TransactionalEventListener
    public void onApplicationEvent(ProjectCreatedEvent event) {
        log.debug("处理项目已创建事件：{}", event);
        createTasks(event.getProject().getId(), TaskType.INVESTMENT_AGREEMENT_PREPARATION);
    }

    public void createTasks(long id, TaskType type) {
        List<TaskTemplate> taskTemplates = taskTemplateManager.listByType(TaskTemplateRefType.PROJECT, type);
        taskTemplates.forEach(i -> {
            Task task = new Task();
            task.setDepartment(i.getDepartment());
            task.setType(i.getType());
            task.setAssigneeId(i.getAssigneeId());
            task.setCompletedAt("0");
            task.setRefId(id);
            task.setStatus(TaskStatusType.pending);
            task.setTaskTemplateId(i.getId());
            taskMapper.insert(task);
        });
    }

}
