package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.PasswordChangeRequest;
import eco.ywhc.xr.common.model.RequestContextUser;
import eco.ywhc.xr.core.manager.UserManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final String SESSION_ATTRIBUTE_USER = "user";

    private final UserManager userManager;

    @Override
    public String usernamePasswordAuthenticate(HttpServletRequest httpServletRequest, String username, String rawPassword) {
        RequestContextUser user = userManager.usernamePasswordAuthenticate(username, rawPassword);
        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(SESSION_ATTRIBUTE_USER, user);
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

}
