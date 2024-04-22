package eco.ywhc.xr.core.event;

import eco.ywhc.xr.common.constant.TaskStatusType;
import eco.ywhc.xr.common.constant.TaskTemplateRefType;
import eco.ywhc.xr.common.constant.TaskType;
import eco.ywhc.xr.common.converter.TaskConverter;
import eco.ywhc.xr.common.event.FrameworkAgreementCreatedEvent;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.core.manager.TaskTemplateManager;
import eco.ywhc.xr.core.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

/**
 * 框架协议相关的事件
 */
@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class FrameworkAgreementEventListener {

    private final TaskConverter taskConverter;

    private final TaskTemplateManager taskTemplateManager;

    private final TaskMapper taskMapper;

    @Async
    @TransactionalEventListener
    public void onApplicationEvent(FrameworkAgreementCreatedEvent event) {
        log.debug("处理框架协议已创建事件：{}", event);
        createTasks(event.getFrameworkAgreement().getId(), TaskType.PROJECT_PROPOSAL_PREPARATION);
        createTasks(event.getFrameworkAgreement().getId(), TaskType.FRAMEWORK_AGREEMENT_PREPARATION);
    }

    public void createTasks(long id, TaskType type) {
        List<Task> tasks = taskTemplateManager.listByType(TaskTemplateRefType.FRAMEWORK_AGREEMENT, type).stream()
                .map(i -> {
                    Task task = taskConverter.fromTaskTemplate(i);
                    task.setCompletedAt("0");
                    task.setRefId(id);
                    task.setStatus(TaskStatusType.pending);
                    task.setTaskTemplateId(i.getId());
                    return task;
                })
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        taskMapper.bulkInsert(tasks);
    }

}
