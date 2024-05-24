package eco.ywhc.xr.core.event;

import eco.ywhc.xr.common.constant.*;
import eco.ywhc.xr.common.converter.ApprovalConverter;
import eco.ywhc.xr.common.event.FrameworkAgreementCreatedEvent;
import eco.ywhc.xr.common.event.FrameworkAgreementUpdatedEvent;
import eco.ywhc.xr.common.model.entity.Approval;
import eco.ywhc.xr.common.model.entity.FrameworkAgreement;
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
 * 框架协议相关的事件
 */
@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class FrameworkAgreementEventListener {

    private final ApprovalConverter approvalConverter;

    private final TaskTemplateManager taskTemplateManager;

    private final ApprovalTemplateManager approvalTemplateManager;

    private final TaskMapper taskMapper;

    private final ApprovalMapper approvalMapper;

    private final InstanceRoleManager instanceRoleManager;

    private final TaskManager taskManager;

    @EventListener
    public void onApplicationEvent(FrameworkAgreementCreatedEvent event) {
        log.debug("处理框架协议已创建事件：{}", event);
        final FrameworkAgreement frameworkAgreement = event.getFrameworkAgreement();

        // 创建任务清单
        String taskListGuid = taskManager.createTaskList(TaskTemplateRefType.FRAMEWORK_AGREEMENT, frameworkAgreement.getName());
        // 创建项目建议书拟定分组
        String projectProposalPreparationGuid = taskManager.createTaskListSection(taskListGuid, TaskType.PROJECT_PROPOSAL_PREPARATION);
        // 创建框架协议拟定分组
        String frameworkAgreementPreparationGuid = taskManager.createTaskListSection(taskListGuid, TaskType.FRAMEWORK_AGREEMENT_PREPARATION);

        createTasks(frameworkAgreement.getId(), TaskType.PROJECT_PROPOSAL_PREPARATION, taskListGuid, projectProposalPreparationGuid);
        createTasks(frameworkAgreement.getId(), TaskType.FRAMEWORK_AGREEMENT_PREPARATION, taskListGuid, frameworkAgreementPreparationGuid);

        createApprovals(frameworkAgreement.getId(), ApprovalType.PROJECT_PROPOSAL_APPROVAL);
        createApprovals(frameworkAgreement.getId(), ApprovalType.FRAMEWORK_AGREEMENT_APPROVAL);

        instanceRoleManager.assignInstanceRoleToAssignee(frameworkAgreement.getId(), InstanceRefType.FRAMEWORK_AGREEMENT, frameworkAgreement.getAssigneeId());
    }

    @TransactionalEventListener
    public void onApplicationEvent(FrameworkAgreementUpdatedEvent event) {
        log.debug("处理框架协议已更新事件：{}", event);
        final FrameworkAgreement frameworkAgreement = event.getFrameworkAgreement();
        instanceRoleManager.reAssignInstanceRoleToAssignee(frameworkAgreement.getId(), InstanceRefType.FRAMEWORK_AGREEMENT, frameworkAgreement.getAssigneeId());
    }

    public void createTasks(long id, TaskType type, String taskListGuid, String sectionGuid) {
        List<Task> tasks = taskTemplateManager.listByType(TaskTemplateRefType.FRAMEWORK_AGREEMENT, type).stream()
                .map(i -> {
                    Task task = new Task();
                    task.setInstanceRoleId(i.getInstanceRoleId());
                    task.setCompletedAt("0");
                    task.setRefId(id);
                    task.setStatus(TaskStatusType.pending);
                    task.setType(i.getType());
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
        List<Approval> approvals = approvalTemplateManager.listByType(ApprovalTemplateRefType.FRAMEWORK_AGREEMENT, type).stream()
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
