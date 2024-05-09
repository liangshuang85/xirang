package eco.ywhc.xr.common.event;

import eco.ywhc.xr.common.constant.InstanceRefType;
import lombok.*;

import java.time.OffsetDateTime;

/**
 * 事件：状态已变更
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusChangedEvent {

    /**
     * 关联ID
     */
    private Long refId;

    /**
     * 实例关联类型
     */
    private InstanceRefType refType;

    /**
     * 旧状态
     */
    private String before;

    /**
     * 新状态
     */
    private String after;

    /**
     * 操作人ID
     */
    private String operatorId;

    /**
     * 最后改动时间
     */
    private OffsetDateTime lastModifiedAt;

}
