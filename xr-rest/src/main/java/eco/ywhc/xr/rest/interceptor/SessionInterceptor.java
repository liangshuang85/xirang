package eco.ywhc.xr.rest.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import eco.ywhc.xr.common.constant.SessionAttribute;
import eco.ywhc.xr.common.exception.ErrorMessage;
import eco.ywhc.xr.common.security.CurrentUser;
import eco.ywhc.xr.common.security.SecurityContext;
import eco.ywhc.xr.common.security.SecurityContextHolder;
import eco.ywhc.xr.core.manager.JwtManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
@RequiredArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    private final JwtManager jwtManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查 X-Auth-Token 标头
        final String xAuthToken = request.getHeader("x-auth-token");
        if (StringUtils.isNotBlank(xAuthToken)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                final CurrentUser currentUser = extractCurrentUserFromHttpSession(session);
                SecurityContextHolder.setContext(new SecurityContext(currentUser));
                return true;
            }
        }
        // 检查 Authorization 标头
        final String headerValue = request.getHeader("authorization");
        final String token = extractTokenFromBearerAuthentication(headerValue);
        if (StringUtils.isNotBlank(token)) {
            if (jwtManager.validateJWT(token)) {
                return true;
            }
        }

        String errorCode, messageStr;
        if (StringUtils.isBlank(xAuthToken) && StringUtils.isBlank(headerValue)) {
            errorCode = ErrorMessage.ErrorCode.NoAuthorizationHeader.name();
            messageStr = "Unauthorized request.";
        } else {
            errorCode = ErrorMessage.ErrorCode.InvalidAuthorizationHeader.name();
            messageStr = "Bad credentials.";
        }
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code(errorCode).message(ErrorMessage.Message.of(messageStr, request.getRequestURI()))
                .build();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(errorMessage));
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.clearContext();
    }

    private static String extractTokenFromBearerAuthentication(String headerValue) {
        if (StringUtils.isNotBlank(headerValue) && headerValue.startsWith("Bearer")) {
            return headerValue.substring(7);
        }
        return "";
    }

    private static CurrentUser extractCurrentUserFromHttpSession(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object attribute = session.getAttribute(SessionAttribute.SESSION_ATTR_USER);
        if (attribute == null) {
            return null;
        }
        return (CurrentUser) attribute;
    }

}
