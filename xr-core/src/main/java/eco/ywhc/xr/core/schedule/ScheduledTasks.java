package eco.ywhc.xr.core.schedule;

import com.lark.oapi.service.ehr.v1.model.Employee;
import com.lark.oapi.service.ehr.v1.model.ListEmployeeRespBody;
import eco.ywhc.xr.common.constant.TaskStatusType;
import eco.ywhc.xr.common.converter.ApprovalConverter;
import eco.ywhc.xr.common.exception.LarkTaskNotFoundException;
import eco.ywhc.xr.common.model.entity.Approval;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.core.manager.ApprovalManager;
import eco.ywhc.xr.core.manager.TaskManager;
import eco.ywhc.xr.core.manager.lark.LarkDepartmentManager;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledTasks {

    private static final int BATCH_SIZE = 10;

    private final ThreadPoolTaskScheduler taskScheduler;

    private final LarkEmployeeManager larkEmployeeManager;

    private final LarkDepartmentManager larkDepartmentManager;

    private final TaskManager taskManager;

    private final ApprovalManager approvalManager;

    private final ApprovalConverter approvalConverter;

    @Scheduled(initialDelay = 10000, fixedDelay = 30000)
    public void syncLarkEmployees() {
        log.debug("开始同步飞书上的员工花名册信息");
        Boolean hasMore;
        String pageToken = null;
        do {
            ListEmployeeRespBody listEmployeeRespBody = larkEmployeeManager.listEmployees(pageToken);
            if (listEmployeeRespBody == null) {
                break;
            }
            var employees = listEmployeeRespBody.getItems();
            if (employees.length > 0) {
                String[] employeeUserIds = Arrays.stream(employees).map(Employee::getUserId).toArray(String[]::new);
                larkEmployeeManager.appendLarkEmployeeUserId(employeeUserIds);
                taskScheduler.submit(new Thread(() -> {
                    for (var employee : employees) {
                        larkEmployeeManager.retrieveLarkEmployee(employee.getUserId());
                        LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployeeSync(employee.getUserId());
                        larkEmployeeManager.upsertLarkDepartmentEmployees(larkEmployee);
                    }
                }));
            }
            hasMore = listEmployeeRespBody.getHasMore();
            pageToken = listEmployeeRespBody.getPageToken();
        } while (Boolean.TRUE.equals(hasMore) && pageToken != null);
        log.debug("员工花名册信息同步完成");
    }

    @Scheduled(initialDelay = 15000, fixedDelay = 300000)
    public void syncLarkTasks() {
        log.info("开始同步飞书任务信息");
        long prevTaskId = -1;
        while (true) {
            List<Task> tasks = taskManager.listTasks(prevTaskId, BATCH_SIZE);
            if (tasks.isEmpty()) {
                log.debug("飞书任务信息同步完成");
                break;
            }
            for (Task task : tasks) {
                log.debug("开始同步飞书任务信息，任务ID: {}", task.getId());
                try {
                    taskManager.getLarkTask(task);
                } catch (LarkTaskNotFoundException ignored) {
                    task.setTaskGuid(null);
                    task.setStatus(TaskStatusType.deleted);
                    taskManager.updateById(task);
                }
            }
            prevTaskId = tasks.get(tasks.size() - 1).getId();
        }
    }

    @Scheduled(initialDelay = 20000, fixedDelay = 300000)
    public void syncLarkApprovals() {
        log.info("开始同步飞书审批信息");
        long prevApprovalId = -1;
        while (true) {
            List<Approval> approvals = approvalManager.listApprovals(prevApprovalId, BATCH_SIZE);
            if (approvals.isEmpty()) {
                log.debug("飞书审批信息同步完成");
                break;
            }
            for (Approval approval : approvals) {
                log.debug("开始同步飞书审批信息，审批ID: {}", approval.getId());
                approvalManager.updateApproval(approvalConverter.toResponse(approval));
            }
            prevApprovalId = approvals.get(approvals.size() - 1).getId();
        }
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 60000)
    public void syncLarkDepartments() {
        larkDepartmentManager.syncLarkDepartments();
    }

}
