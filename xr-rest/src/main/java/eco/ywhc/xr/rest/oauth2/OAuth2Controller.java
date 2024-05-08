package eco.ywhc.xr.rest.oauth2;

import eco.ywhc.xr.common.model.AuthToken;
import eco.ywhc.xr.common.model.ExchangeTokenRequest;
import eco.ywhc.xr.common.model.OAuth2Callback;
import eco.ywhc.xr.common.model.OAuth2Redirection;
import eco.ywhc.xr.core.config.property.LarkProperties;
import eco.ywhc.xr.core.manager.OAuth2Manager;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.sugar.commons.exception.InvalidInputException;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * OAuth2相关接口
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Tag(name = "OAuth2相关接口")
public class OAuth2Controller {

    private static final String larkAuthBaseUrl = "https://open.feishu.cn/open-apis/authen/v1/authorize";

    private final LarkProperties larkProperties;

    private final OAuth2Manager oauth2Manager;

    /**
     * 获取OAuth2授权地址或者直接跳转到授权地址
     *
     * @param redirect    是否直接重定向，默认为{@code false}
     * @param redirectUri 回调地址
     */
    @GetMapping("/oauth2/authorize")
    public ResponseEntity<OAuth2Redirection> authorize(HttpServletRequest request,
                                                       @RequestParam(required = false, defaultValue = "false") boolean redirect,
                                                       @RequestParam(required = false) String redirectUri) {
        if (StringUtils.isBlank(redirectUri)) {
            redirectUri = ServletUriComponentsBuilder.fromRequest(request)
                    .replacePath("/oauth2/callback")
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
        }
        String state = UUID.randomUUID().toString();
        oauth2Manager.storeOAuth2State(state);
        String authorizeUri = UriComponentsBuilder.fromUriString(larkAuthBaseUrl)
                .queryParam("app_id", larkProperties.getAppId())
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", "")
                .queryParam("state", state)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
        if (redirect) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, authorizeUri);
            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
        }
        return ResponseEntity.ok(OAuth2Redirection.of(redirectUri, authorizeUri));
    }

    /**
     * OAuth2回调
     *
     * @param code  授权码
     * @param state 回调{@code state}参数
     */
    @Hidden
    @GetMapping("/oauth2/callback")
    public ResponseEntity<OAuth2Callback> callback(HttpServletRequest request,
                                                   @RequestParam(value = "code") String code,
                                                   @RequestParam(value = "state", required = false) String state) {
        if (StringUtils.isBlank(code)) {
            throw new InvalidInputException("code参数的值不能为空");
        }
        if (StringUtils.isBlank(state)) {
            throw new InvalidInputException("state参数的值不能为空");
        }
        String okFlag = oauth2Manager.retrieveOAuth2State(state);
        if (!oauth2Manager.OK_FLAG.equals(okFlag)) {
            throw new InvalidInputException("state参数错误");
        }
        String userAccessToken = oauth2Manager.handleOAuth2Callback(code);
        if (StringUtils.isBlank(userAccessToken)) {
            throw new InvalidInputException("code参数错误");
        }
        request.getSession(true);
        return ResponseEntity.ok(OAuth2Callback.of("ok"));
    }

    /**
     * 通过OAuth2授权成功后返回的授权码换取系统的X-Auth-Token
     */
    @PostMapping("/oauth2/token")
    public ResponseEntity<AuthToken> exchange(HttpServletRequest request,
                                              @Valid @RequestBody ExchangeTokenRequest exchangeTokenRequest) {
        String okFlag = oauth2Manager.retrieveOAuth2State(exchangeTokenRequest.getState());
        if (!oauth2Manager.OK_FLAG.equals(okFlag)) {
            throw new InvalidInputException("state参数错误");
        }
        String userAccessToken = oauth2Manager.handleOAuth2Callback(exchangeTokenRequest.getCode());
        if (StringUtils.isBlank(userAccessToken)) {
            throw new InvalidInputException("code参数错误");
        }
        HttpSession session = request.getSession(true);
        return ResponseEntity.ok(AuthToken.of(session.getId()));
    }

}
