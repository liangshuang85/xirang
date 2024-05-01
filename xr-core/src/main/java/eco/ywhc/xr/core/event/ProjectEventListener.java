package eco.ywhc.xr.core.event;

import eco.ywhc.xr.common.constant.*;
import eco.ywhc.xr.common.converter.ApprovalConverter;
import eco.ywhc.xr.common.converter.TaskConverter;
import eco.ywhc.xr.common.event.ProjectCreatedEvent;
import eco.ywhc.xr.common.model.dto.res.DepartmentRes;
import eco.ywhc.xr.common.model.entity.Approval;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.core.manager.ApprovalTemplateManager;
import eco.ywhc.xr.core.manager.TaskManager;
import eco.ywhc.xr.core.manager.TaskTemplateManager;
import eco.ywhc.xr.core.mapper.ApprovalMapper;
import eco.ywhc.xr.core.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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

    private final TaskConverter taskConverter;

    private final ApprovalConverter approvalConverter;

    private final TaskTemplateManager taskTemplateManager;

    private final ApprovalTemplateManager approvalTemplateManager;

    private final TaskMapper taskMapper;

    private final ApprovalMapper approvalMapper;

    private final TaskManager taskManager;

    @TransactionalEventListener
    public void onApplicationEvent(ProjectCreatedEvent event) {
        log.debug("处理项目已创建事件：{}", event);
        createTasks(event.getProject().getId(), TaskType.INVESTMENT_AGREEMENT_PREPARATION);

        createApprovals(event.getProject().getId(), ApprovalType.INVESTMENT_AGREEMENT_APPROVAL);
        createApprovals(event.getProject().getId(), ApprovalType.INVESTMENT_AGREEMENT_SIGN_APPROVAL);
    }

    public void createTasks(long id, TaskType type) {
        List<Task> tasks = taskTemplateManager.listByType(TaskTemplateRefType.PROJECT, type).stream()
                .map(i -> {
                    Task task = new Task();
                    DepartmentRes departmentRes = taskManager.getDepartmentByDepartmentId(i.getDepartmentId());
                    task.setDepartment(departmentRes.getName());
                    task.setAssigneeId(departmentRes.getLeaderUserId());
                    task.setType(i.getType());
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
