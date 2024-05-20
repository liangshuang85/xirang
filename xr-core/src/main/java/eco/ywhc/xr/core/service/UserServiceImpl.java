package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.PasswordChangeRequest;
import eco.ywhc.xr.common.model.RequestContextUser;
import eco.ywhc.xr.core.manager.OAuth2Manager;
import eco.ywhc.xr.core.manager.RoleManager;
import eco.ywhc.xr.core.manager.UserManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;
import org.sugar.commons.exception.InvalidInputException;

import java.util.Collections;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final String SESSION_ATTRIBUTE_USER = "user";

    public static final String SESSION_ATTRIBUTE_PERMISSION = "permission";

    private final UserManager userManager;

    private final OAuth2Manager oauth2Manager;

    private final RoleManager roleManager;

    @Override
    public String usernamePasswordAuthenticate(HttpServletRequest httpServletRequest, String username, String rawPassword) {
        RequestContextUser user = userManager.usernamePasswordAuthenticate(username, rawPassword);
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(SESSION_ATTRIBUTE_USER, user);
        return session.getId();
    }

    @Override
    public String oauth2Authenticate(HttpServletRequest httpServletRequest, String authorizationCode, String state) {
        String okFlag = oauth2Manager.retrieveOAuth2State(state);
        if (!oauth2Manager.OK_FLAG.equals(okFlag)) {
            throw new InvalidInputException("state参数错误");
        }
        String userAccessToken = oauth2Manager.handleOAuth2Callback(authorizationCode);
        if (StringUtils.isBlank(userAccessToken)) {
            throw new InvalidInputException("code参数错误");
        }
        String userOpenId = oauth2Manager.getUserOpenId(userAccessToken);
        if (StringUtils.isBlank(userOpenId)) {
            throw new InternalErrorException("获取Access Token失败");
        }

        RequestContextUser user = userManager.authWithUserOpenId(userOpenId);
        // 获取用户的角色和权限
        Set<Long> roleIds = roleManager.listAssignedRoleIds(userOpenId);
        Set<String> permissionCodes = roleManager.listGrantedPermissionCodes(roleIds);

        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(SESSION_ATTRIBUTE_USER, user);
        session.setAttribute(SESSION_ATTRIBUTE_PERMISSION, permissionCodes);
        return session.getId();
    }

    @Override
    public boolean changePassword(HttpServletRequest httpServletRequest, PasswordChangeRequest req) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            return false;
        }
        RequestContextUser requestContextUser = (RequestContextUser) session.getAttribute(SESSION_ATTRIBUTE_USER);
        if (requestContextUser == null) {
            return false;
        }
        return userManager.changePassword(requestContextUser.getId(), req);
    }

    @Override
    public Set<String> listMyPermissionCodes(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            return Collections.emptySet();
        }
        Object permissions = session.getAttribute(SESSION_ATTRIBUTE_PERMISSION);
        if (permissions == null) {
            return Collections.emptySet();
        }
        return (Set<String>) permissions;
    }

}
