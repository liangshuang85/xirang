package eco.ywhc.xr.core.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统事件
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SystemEventListener {

    @EventListener
    @Transactional(rollbackFor = {Exception.class})
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.trace("系统准备就绪");
    }

}
