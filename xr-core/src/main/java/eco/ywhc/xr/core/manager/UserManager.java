package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.exception.InvalidCredentialException;
import eco.ywhc.xr.common.model.PasswordChangeRequest;
import eco.ywhc.xr.common.model.RequestContextUser;
import eco.ywhc.xr.common.model.entity.User;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户
 */
@Transactional(rollbackFor = {Exception.class})
public interface UserManager {

    /**
     * 通过ID查找用户
     */
    @Nullable
    User findEntityById(final long id);

    /**
     * 通过用户名查找用户
     */
    @Nullable
    User findEntityByUsername(final String username);

    /**
     * 用户名密码认证
     *
     * @param username    用户名
     * @param rawPassword 密码原文
     * @return RequestContextUser 如果认证成功，返回{@link RequestContextUser}实例，否则抛出{@link InvalidCredentialException}异常
     */
    RequestContextUser usernamePasswordAuthenticate(String username, String rawPassword) throws InvalidCredentialException;

    /**
     * 修改密码
     *
     * @param id 用户ID
     */
    boolean changePassword(long id, PasswordChangeRequest req);

}
