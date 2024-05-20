package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.core.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * 当前登录用户管理接口
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class MyselfController {

    private final UserService userService;

    /**
     * 获取当前登录用户已被授予的权限的编码
     */
    @GetMapping("/me/permissionCodes")
    public Set<String> listMyPermissionCodes(HttpServletRequest httpServletRequest) {
        return userService.listMyPermissionCodes(httpServletRequest);
    }

}
