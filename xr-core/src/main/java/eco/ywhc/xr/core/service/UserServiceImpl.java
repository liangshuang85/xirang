package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.constant.SessionAttribute;
import eco.ywhc.xr.common.model.PasswordChangeRequest;
import eco.ywhc.xr.common.model.RequestContextUser;
import eco.ywhc.xr.core.manager.OAuth2Manager;
import eco.ywhc.xr.core.manager.RoleManager;
import eco.ywhc.xr.core.manager.UserManager;
import eco.ywhc.xr.core.util.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;
import org.sugar.commons.exception.InvalidInputException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserManager userManager;

    private final OAuth2Manager oauth2Manager;

    private final RoleManager roleManager;

    @Override
    public String usernamePasswordAuthenticate(HttpServletRequest httpServletRequest, String username, String rawPassword) {
        RequestContextUser user = userManager.usernamePasswordAuthenticate(username, rawPassword);
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(SessionAttribute.SESSION_ATTR_USER, user);
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
        // 获取基本角色权限
        Set<String> basicPermissionCodes = roleManager.listBasicPermissionCodes();
        // 获取用户的角色和权限
        Set<Long> roleIds = roleManager.listAssignedRoleIds(userOpenId);
        Set<String> permissionCodes = roleManager.listGrantedPermissionCodes(roleIds);
        // 得到当前用户的最终权限
        Set<String> combinedPermissionCodes = Stream.concat(basicPermissionCodes.stream(), permissionCodes.stream())
                .collect(Collectors.toSet());

        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(SessionAttribute.SESSION_ATTR_USER, user);
        session.setAttribute(SessionAttribute.SESSION_ATTR_PERMISSION, combinedPermissionCodes);
        return session.getId();
    }

    @Override
    public boolean changePassword(PasswordChangeRequest req) {
        RequestContextUser requestContextUser = SessionUtils.currentUser();
        if (requestContextUser == null) {
            return false;
        }
        return userManager.changePassword(requestContextUser.getId(), req);
    }

    @Override
    public List<String> listMyPermissionCodes() {
        return SessionUtils.currentUserPermissionCodes().stream().sorted().toList();
    }

}
