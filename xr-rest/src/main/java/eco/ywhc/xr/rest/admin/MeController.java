package eco.ywhc.xr.rest.admin;

import eco.ywhc.xr.common.model.PasswordChangeRequest;
import eco.ywhc.xr.core.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.sugar.crud.model.OperationResult;

/**
 * 当前登录用户管理接口
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class MeController {

    private final UserService userService;

    /**
     * 修改当前登录用户的密码
     */
    @PostMapping("/admin/rest/me:changePassword")
    public OperationResult changePassword(HttpServletRequest httpServletRequest,
                                          @Valid @RequestBody PasswordChangeRequest req) {
        boolean ok = userService.changePassword(req);
        int affected = ok ? 1 : 0;
        return OperationResult.of(affected);
    }

}
