package eco.ywhc.xr.core.util;

import eco.ywhc.xr.common.model.RequestContextUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Session工具类
 */
public class SessionUtils {

    public static final String SESSION_ATTRIBUTE_USER = "user";

    /**
     * 获取当前请求关联的用户信息
     */
    public static RequestContextUser getUserinfo() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = requestAttributes.getRequest().getSession(false);
        Object attribute = httpSession.getAttribute(SESSION_ATTRIBUTE_USER);
        if (attribute == null) {
            return null;
        }
        return (RequestContextUser) attribute;
    }

}
