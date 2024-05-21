package eco.ywhc.xr.common.event;

import eco.ywhc.xr.common.model.entity.FrameworkAgreement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 事件：框架协议已更新
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor(staticName = "of")
public class FrameworkAgreementUpdatedEvent {

    /**
     * 框架协议
     */
    private final FrameworkAgreement frameworkAgreement;

}
