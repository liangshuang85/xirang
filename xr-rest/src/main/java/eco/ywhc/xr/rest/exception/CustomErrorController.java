package eco.ywhc.xr.rest.exception;

import eco.ywhc.xr.common.exception.ErrorMessage;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 替换Spring boot默认的错误响应
 */
@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ErrorMessage handleError(HttpServletRequest request) {
        String messageStr = "The server encountered an unexpected error.";
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                messageStr = "The specified resource does not exist.";
                return ErrorMessage.builder()
                        .code(ErrorMessage.ErrorCode.ResourceNotFound.name())
                        .message(ErrorMessage.Message.of(messageStr, request.getRequestURI()))
                        .build();
            }
        }
        return ErrorMessage.builder()
                .code(ErrorMessage.ErrorCode.InternalError.name())
                .message(ErrorMessage.Message.of(messageStr, request.getRequestURI()))
                .build();
    }

}
