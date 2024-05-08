package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.PasswordChangeRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户
 */
@Transactional(rollbackFor = {Exception.class})
public interface UserService {

    /**
     * 通过用户名和密码进行认证
     *
     * @param username    用户名
     * @param rawPassword 密码原文
     * @return 认证成功返回Session ID
     */
    String usernamePasswordAuthenticate(HttpServletRequest httpServletRequest, String username, String rawPassword);

    /**
     * 修改当前登录用户的密码
     */
    boolean changePassword(HttpServletRequest httpServletRequest, PasswordChangeRequest req);

}
