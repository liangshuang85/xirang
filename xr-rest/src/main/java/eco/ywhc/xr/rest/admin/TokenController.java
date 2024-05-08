package eco.ywhc.xr.rest.admin;

import eco.ywhc.xr.common.model.AuthRequest;
import eco.ywhc.xr.common.model.AuthToken;
import eco.ywhc.xr.core.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TokenController {

    private final UserService userService;

    @PostMapping("/admin/rest/tokens")
    public ResponseEntity<AuthToken> login(HttpServletRequest request,
                                           @Valid @RequestBody AuthRequest authRequest) {
        String sessionId = userService.usernamePasswordAuthenticate(request, authRequest.getIdentity(), authRequest.getPassword());
        return ResponseEntity.ok(AuthToken.of(sessionId));
    }

}
