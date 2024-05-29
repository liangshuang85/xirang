package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.converter.UserConverter;
import eco.ywhc.xr.common.exception.InvalidCredentialException;
import eco.ywhc.xr.common.model.PasswordChangeRequest;
import eco.ywhc.xr.common.security.CurrentUser;
import eco.ywhc.xr.common.model.entity.User;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import eco.ywhc.xr.core.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.ConditionNotMetException;
import org.sugar.commons.exception.InvalidInputException;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserManagerImpl implements UserManager {

    public static final String COMMON_AUTH_FAILED_MSG = "您提供用户名、密码组合错误，无法登录系统。";

    private final UserConverter userConverter;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final LarkEmployeeManager larkEmployeeManager;

    @Override
    public User findEntityById(long id) {
        return userMapper.findEntityById(id);
    }

    @Override
    public User findEntityByUsername(String username) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getDeleted, 0).eq(User::getUsername, username);
        return userMapper.selectOne(qw);
    }

    @Override
    public User findEntityByLarkOpenId(String larkOpenId) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getDeleted, 0).eq(User::getLarkOpenId, larkOpenId);
        return userMapper.selectOne(qw);
    }

    @Override
    public CurrentUser usernamePasswordAuthenticate(String username, String rawPassword) throws InvalidCredentialException {
        log.debug("尝试使用用户名\"{}\"登录系统", username);
        if (StringUtils.isBlank(username)) {
            log.debug("登录提供的用户名为空");
            throw new InvalidCredentialException(COMMON_AUTH_FAILED_MSG);
        }
        if (StringUtils.isBlank(rawPassword)) {
            log.debug("登录提供的密码为空");
            throw new InvalidCredentialException(COMMON_AUTH_FAILED_MSG);
        }
        User user = findEntityByUsername(username);
        if (user == null) {
            log.debug("用户不存在");
            throw new InvalidCredentialException(COMMON_AUTH_FAILED_MSG);
        }
        if (StringUtils.isBlank(user.getPasswordHash())) {
            log.debug("用户[username={}]的密码哈希为空", username);
            throw new InvalidCredentialException(COMMON_AUTH_FAILED_MSG);
        }
        if (user.getBlocked() != null && user.getBlocked()) {
            log.debug("用户[username={}]已被禁止登录", username);
            throw new InvalidCredentialException(COMMON_AUTH_FAILED_MSG);
        }
        boolean ok = passwordEncoder.matches(rawPassword, user.getPasswordHash());
        if (!ok) {
            throw new InvalidCredentialException(COMMON_AUTH_FAILED_MSG);
        }
        return userConverter.toCurrentUser(user);
    }

    @Override
    public CurrentUser authWithUserOpenId(@NonNull String userOpenId) {
        log.debug("飞书用户的OpenID\"{}\"登录系统", userOpenId);
        if (StringUtils.isBlank(userOpenId)) {
            log.warn("登录提供的用户OpenID为空");
            throw new InvalidCredentialException("登录失败");
        }
        LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployeeSync(userOpenId);
        if (larkEmployee == null) {
            log.warn("获取用户信息失败");
            throw new InvalidCredentialException("登录失败");
        }
        User user = findEntityByLarkOpenId(userOpenId);
        if (user == null) {
            user = new User();
            user.setUsername(userOpenId);
            user.setFullName(larkEmployee.getName());
            user.setPhoneNumber(larkEmployee.getMobile());
            user.setEmail(larkEmployee.getEmail());
            user.setLarkOpenId(userOpenId);
            user.setAdmin(false);
            user.setBlocked(false);
            userMapper.insert(user);
        }
        return userConverter.toCurrentUser(user);
    }

    @Override
    public boolean changePassword(long id, PasswordChangeRequest req) {
        if (StringUtils.isEmpty(req.getCurrentPassword())) {
            throw new InvalidInputException("当前密码错误");
        }
        if (!Objects.equals(req.getNewPassword(), req.getNewPasswordConfirmation())) {
            throw new InvalidInputException("您输入的两次新密码不相同");
        }
        User user = findEntityById(id);
        if (user == null || user.getBlocked()) {
            throw new ConditionNotMetException("您当前不能修改密码");
        }
        boolean ok = passwordEncoder.matches(req.getCurrentPassword(), user.getPasswordHash());
        if (!ok) {
            throw new ConditionNotMetException("当前密码错误");
        }
        changePassword(id, req.getNewPassword());
        return true;
    }

    private void changePassword(long id, String newPassword) {
        UpdateWrapper<User> uw = new UpdateWrapper<>();
        uw.lambda().eq(User::getDeleted, 0)
                .eq(User::getId, id)
                .set(User::getPasswordHash, passwordEncoder.encode(newPassword));
        userMapper.update(uw);
    }

}
