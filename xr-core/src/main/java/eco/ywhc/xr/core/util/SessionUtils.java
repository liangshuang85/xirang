package eco.ywhc.xr.core.util;

import eco.ywhc.xr.common.constant.SessionAttribute;
import eco.ywhc.xr.common.model.RequestContextUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Collection;
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
    public static RequestContextUser currentUser() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = requestAttributes.getRequest().getSession(false);
        Object attribute = httpSession.getAttribute(SessionAttribute.SESSION_ATTR_USER);
        return (RequestContextUser) attribute;
    }

    /**
     * 获取当前请求关联的用户信息
     */
    public static List<String> currentUserPermissionCodes() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = requestAttributes.getRequest().getSession(false);
        if (httpSession == null) {
            return Collections.emptyList();
        }
        Object attribute = httpSession.getAttribute(SessionAttribute.SESSION_ATTR_PERMISSION);
        if (attribute == null) {
            return Collections.emptyList();
        }
        List<String> permissions = new ArrayList<>();
        if (attribute instanceof Collection<?> collection) {
            for (Object o : collection) {
                permissions.add((String) o);
            }
        }
        return permissions;
    }

}
