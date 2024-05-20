package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.PasswordChangeRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

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
     * 通过授权码进行认证
     *
     * @param authorizationCode 授权码
     * @param state             {@code state}参数
     */
    String oauth2Authenticate(HttpServletRequest httpServletRequest, String authorizationCode, String state);

    /**
     * 修改当前登录用户的密码
     */
    boolean changePassword(HttpServletRequest httpServletRequest, PasswordChangeRequest req);

    /**
     * 列出当前用户所获得的全部权限的权限编码
     */
    Set<String> listMyPermissionCodes(HttpServletRequest httpServletRequest);

}
