package eco.ywhc.xr.common.event;

import eco.ywhc.xr.common.model.entity.Clue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 事件：线索已创建
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor(staticName = "of")
public class ClueCreatedEvent {

    /**
     * 项目
     */
    private final Clue clue;

}
