package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.constant.SessionAttribute;
import eco.ywhc.xr.common.model.PasswordChangeRequest;
import eco.ywhc.xr.common.security.CurrentUser;
import eco.ywhc.xr.common.security.SecurityContext;
import eco.ywhc.xr.common.security.SecurityContextHolder;
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
        CurrentUser currentUser = userManager.usernamePasswordAuthenticate(username, rawPassword);
        // 获取基本角色权限
        Set<String> basicPermissionCodes = roleManager.listBasicPermissionCodes();
        log.trace("用户所分配的基本权限有：{}", basicPermissionCodes);
        currentUser = currentUser.toBuilder().permissionCodes(basicPermissionCodes).build();
        // 将当前用户的信息保存到 <code>SecurityContext</code>
        SecurityContext securityContext = new SecurityContext(currentUser);
        SecurityContextHolder.setContext(securityContext);

        final CurrentUser currentUser1 = SessionUtils.currentUser();
        System.out.println(currentUser1);

        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(SessionAttribute.SESSION_ATTR_USER, currentUser);
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

        CurrentUser currentUser = userManager.authWithUserOpenId(userOpenId);
        // 获取基本角色权限
        Set<String> basicPermissionCodes = roleManager.listBasicPermissionCodes();
        log.trace("用户所分配的基本权限有：{}", basicPermissionCodes);
        // 获取用户的角色和权限
        Set<Long> roleIds = roleManager.listAssignedRoleIds(userOpenId);
        log.trace("用户所分配的角色有：{}", roleIds);
        Set<String> permissionCodes = roleManager.listGrantedPermissionCodes(roleIds);
        log.trace("用户所分配的角色对应的权限有：{}", permissionCodes);
        // 得到当前用户的最终权限
        Set<String> combinedPermissionCodes = Stream.concat(basicPermissionCodes.stream(), permissionCodes.stream())
                .collect(Collectors.toSet());
        log.trace("用户所分配的最终权限是：{}", combinedPermissionCodes);
        currentUser = currentUser.toBuilder().permissionCodes(combinedPermissionCodes).build();
        // 将当前用户的信息保存到 <code>SecurityContext</code>
        SecurityContext securityContext = new SecurityContext(currentUser);
        SecurityContextHolder.setContext(securityContext);

        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(SessionAttribute.SESSION_ATTR_USER, currentUser);
        session.setAttribute(SessionAttribute.SESSION_ATTR_PERMISSION, combinedPermissionCodes);
        return session.getId();
    }

    @Override
    public boolean changePassword(PasswordChangeRequest req) {
        CurrentUser currentUser = SessionUtils.currentUser();
        if (currentUser == null) {
            return false;
        }
        return userManager.changePassword(currentUser.getId(), req);
    }

    @Override
    public List<String> listMyPermissionCodes() {
        return SessionUtils.currentUserPermissionCodes().stream().sorted().toList();
    }

}
