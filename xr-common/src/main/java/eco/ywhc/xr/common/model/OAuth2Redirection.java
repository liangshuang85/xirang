package eco.ywhc.xr.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * OAuth2重定向对象
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor(staticName = "of")
public class OAuth2Redirection {

    /**
     * 重定向URI
     */
    private final String redirectUri;

    /**
     * 授权URI
     */
    private final String authorizeUri;

}
