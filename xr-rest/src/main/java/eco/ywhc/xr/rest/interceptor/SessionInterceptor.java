package eco.ywhc.xr.rest.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import eco.ywhc.xr.common.exception.ErrorMessage;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String xAuthToken = request.getHeader("x-auth-token");
        if (StringUtils.isNotBlank(xAuthToken)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                return true;
            }
        }

        String errorCode, messageStr;
        if (StringUtils.isBlank(xAuthToken)) {
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

}
