package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.event.StatusChangedEvent;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = {Exception.class})
public interface ChangeService {

    void onApplicationEvent(StatusChangedEvent event);

}
