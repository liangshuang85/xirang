package eco.ywhc.xr.core.event;

import eco.ywhc.xr.common.constant.*;
import eco.ywhc.xr.common.converter.ApprovalConverter;
import eco.ywhc.xr.common.event.ProjectCreatedEvent;
import eco.ywhc.xr.common.event.ProjectUpdatedEvent;
import eco.ywhc.xr.common.model.entity.Approval;
import eco.ywhc.xr.common.model.entity.Project;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.core.manager.ApprovalTemplateManager;
import eco.ywhc.xr.core.manager.InstanceRoleManager;
import eco.ywhc.xr.core.manager.TaskManager;
import eco.ywhc.xr.core.manager.TaskTemplateManager;
import eco.ywhc.xr.core.mapper.ApprovalMapper;
import eco.ywhc.xr.core.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.event.EventListener;
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

    private final ApprovalConverter approvalConverter;

    private final TaskTemplateManager taskTemplateManager;

    private final ApprovalTemplateManager approvalTemplateManager;

    private final TaskMapper taskMapper;

    private final TaskManager taskManager;

    private final ApprovalMapper approvalMapper;

    private final InstanceRoleManager instanceRoleManager;

    @EventListener
    public void onApplicationEvent(ProjectCreatedEvent event) {
        log.debug("处理项目已创建事件：{}", event);
        final Project project = event.getProject();

        // 创建任务清单
        String taskListGuid = taskManager.createTaskList(TaskTemplateRefType.PROJECT, project.getName());
        // 创建投资协议拟定分组
        String projectProposalPreparationGuid = taskManager.createTaskListSection(taskListGuid, TaskType.INVESTMENT_AGREEMENT_PREPARATION);

        createTasks(project.getId(), TaskType.INVESTMENT_AGREEMENT_PREPARATION, taskListGuid, projectProposalPreparationGuid);

        createApprovals(project.getId(), ApprovalType.INVESTMENT_AGREEMENT_APPROVAL);
        createApprovals(project.getId(), ApprovalType.INVESTMENT_AGREEMENT_SIGN_APPROVAL);

        instanceRoleManager.assignInstanceRoleToAssignee(project.getId(), InstanceRefType.PROJECT, project.getAssigneeId());
    }

    @TransactionalEventListener
    public void onApplicationEvent(ProjectUpdatedEvent event) {
        log.debug("处理项目已更新事件：{}", event);
        final Project project = event.getProject();
        instanceRoleManager.reAssignInstanceRoleToAssignee(project.getId(), InstanceRefType.PROJECT, project.getAssigneeId());
    }

    public void createTasks(long id, TaskType type, String taskListGuid, String sectionGuid) {
        List<Task> tasks = taskTemplateManager.listByType(TaskTemplateRefType.PROJECT, type).stream()
                .map(i -> {
                    Task task = new Task();
                    task.setInstanceRoleId(i.getInstanceRoleId());
                    task.setType(i.getType());
                    task.setCompletedAt("0");
                    task.setRefId(id);
                    task.setStatus(TaskStatusType.pending);
                    task.setTaskTemplateId(i.getId());
                    task.setTasklistGuid(taskListGuid);
                    task.setSectionGuid(sectionGuid);
                    return task;
                })
                .toList();
        if (CollectionUtils.isEmpty(tasks)) {
            return;
        }
        taskMapper.bulkInsert(tasks);
    }

    public void createApprovals(long id, ApprovalType type) {
        List<Approval> approvals = approvalTemplateManager.listByType(ApprovalTemplateRefType.PROJECT, type).stream()
                .map(i -> {
                    Approval approval = approvalConverter.fromApprovalTemplate(i);
                    approval.setDepartmentName(i.getDepartment());
                    approval.setRefId(id);
                    approval.setApprovalStatus(ApprovalStatusType.PENDING_START);
                    return approval;
                }).toList();
        if (CollectionUtils.isEmpty(approvals)) {
            return;
        }
        approvalMapper.bulkInsert(approvals);
    }

}
