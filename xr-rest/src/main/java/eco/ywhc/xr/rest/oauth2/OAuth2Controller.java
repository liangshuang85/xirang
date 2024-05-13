package eco.ywhc.xr.rest.oauth2;

import eco.ywhc.xr.common.model.AuthToken;
import eco.ywhc.xr.common.model.ExchangeTokenRequest;
import eco.ywhc.xr.common.model.OAuth2Redirection;
import eco.ywhc.xr.core.config.property.LarkProperties;
import eco.ywhc.xr.core.manager.OAuth2Manager;
import eco.ywhc.xr.core.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.Map;
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

    private final UserService userService;

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
    public ResponseEntity<Map<String, String>> callback(HttpServletRequest request,
                                                        @RequestParam(value = "code") String code,
                                                        @RequestParam(value = "state", required = false) String state) {
        if (StringUtils.isBlank(code)) {
            throw new InvalidInputException("code参数的值不能为空");
        }
        if (StringUtils.isBlank(state)) {
            throw new InvalidInputException("state参数的值不能为空");
        }
        userService.oauth2Authenticate(request, code, state);
        return ResponseEntity.ok(Map.of("message", "ok"));
    }

    /**
     * 通过OAuth2授权成功后返回的授权码换取系统的X-Auth-Token
     */
    @PostMapping("/oauth2/token")
    public ResponseEntity<AuthToken> exchange(HttpServletRequest request,
                                              @Valid @RequestBody ExchangeTokenRequest exchangeTokenRequest) {
        String sessionId = userService.oauth2Authenticate(request, exchangeTokenRequest.getCode(), exchangeTokenRequest.getState());
        return ResponseEntity.ok(AuthToken.of(sessionId));
    }

}
