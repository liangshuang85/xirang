package eco.ywhc.xr.core.util;

import eco.ywhc.xr.common.security.CurrentUser;
import eco.ywhc.xr.common.security.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

/**
 * Session工具类
 */
public class SessionUtils {

    /**
     * 获取当前请求关联的用户信息
     * <p>
     * 需要确保用户已登录才可调用
     */
    public static CurrentUser currentUser() {
        return SecurityContextHolder.getContext().getCurrentUser();
    }

    /**
     * 获取当前请求关联的用户的权限
     */
    public static List<String> currentUserPermissionCodes() {
        final CurrentUser currentUser = currentUser();
        if (currentUser == null) {
            return Collections.emptyList();
        }
        return List.copyOf(currentUser.getPermissionCodes());
    }

}
