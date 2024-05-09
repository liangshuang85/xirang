package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.event.StatusChangedEvent;
import eco.ywhc.xr.common.model.entity.Change;
import eco.ywhc.xr.core.mapper.ChangeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChangeServiceImpl implements ChangeService {

    private final ChangeMapper changeMapper;

    @Override
    @TransactionalEventListener
    public void onApplicationEvent(StatusChangedEvent event) {
        handleStatusChange(event);
    }

    private void handleStatusChange(StatusChangedEvent event) {
        Change change = new Change();
        change.setRefId(event.getRefId());
        change.setRefType(event.getRefType());
        change.setBefore(event.getBefore());
        change.setAfter(event.getAfter());
        change.setOperatorId(event.getOperatorId());
        long between = ChronoUnit.DAYS.between(event.getLastModifiedAt(), OffsetDateTime.now());
        change.setElapsedDays((int) between);
        changeMapper.insert(change);
    }

}
