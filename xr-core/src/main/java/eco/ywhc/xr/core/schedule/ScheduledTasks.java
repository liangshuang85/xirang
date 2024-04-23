package eco.ywhc.xr.core.schedule;

import com.lark.oapi.service.ehr.v1.model.ListEmployeeRespBody;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledTasks {

    private final ThreadPoolTaskScheduler taskScheduler;

    private final LarkEmployeeManager larkEmployeeManager;

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
                taskScheduler.submit(new Thread(() -> {
                    for (var employee : employees) {
                        larkEmployeeManager.retrieveLarkEmployee(employee.getUserId());
                    }
                }));
            }
            hasMore = listEmployeeRespBody.getHasMore();
            pageToken = listEmployeeRespBody.getPageToken();
        } while (Boolean.TRUE.equals(hasMore) && pageToken != null);
        log.debug("员工花名册信息同步完成");
    }

}
