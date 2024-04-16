package eco.ywhc.xr.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * OAuth2回调对象
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor(staticName = "of")
public class OAuth2Callback {

    /**
     * 消息
     */
    private final String message;

}
