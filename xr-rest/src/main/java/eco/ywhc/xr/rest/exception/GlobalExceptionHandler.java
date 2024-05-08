package eco.ywhc.xr.rest.exception;

import eco.ywhc.xr.common.exception.ErrorMessage;
import eco.ywhc.xr.common.exception.InvalidCredentialException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.sugar.commons.exception.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 全局异常处理
 */
@Hidden
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String INTERNAL_SERVER_ERROR_MSG = "未处理的异常，请联系开发者。";

    /**
     * 500错误。用于处理开发者未捕获到的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleGlobalException(Exception ex, HttpServletRequest request) {
        if (log.isErrorEnabled()) {
            log.error("接口调用出现未处理的Exception: {}\nMessage: {}", ex.getClass().getCanonicalName(), ex.getMessage());
            var cause = ex.getCause();
            if (cause != null) {
                log.debug("Cause: {}", cause.getMessage());
            }
            ex.printStackTrace();
        }
        return ErrorMessage.builder()
                .code(ErrorMessage.ErrorCode.InternalError.name())
                .message(ErrorMessage.Message.of(INTERNAL_SERVER_ERROR_MSG, request.getRequestURI()))
                .build();
    }

    /**
     * 400错误
     */
    @ExceptionHandler({
            AmbiguousInputException.class,
            BindException.class,
            ConstraintViolationException.class,
            ConditionNotMetException.class,
            IllegalArgumentException.class,
            InvalidFileException.class,
            InvalidInputException.class,
            InvalidMediaTypeException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class,
            MissingServletRequestPartException.class,
            MultipartException.class,
            RateLimitExceededException.class,
            UniqueViolationException.class,
    })
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorMessage handle400BadRequest(Exception ex, HttpServletRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("Exception: {}\nMessage: {}", ex.getClass().getCanonicalName(), ex.getMessage());
            var cause = ex.getCause();
            if (cause != null) {
                log.debug("Cause: {}", cause.getMessage());
            }
        }
        String errorCode = ex.getClass().getSimpleName();
        String messageStr = "";
        List<String> errors = new ArrayList<>();
        if (ex instanceof BindException bindException) {
            errorCode = ErrorMessage.ErrorCode.InvalidInput.name();
            int errorCount = bindException.getBindingResult().getErrorCount();
            messageStr = "Validation failed. " + errorCount + " error(s)";
            for (FieldError fieldError : bindException.getBindingResult().getFieldErrors()) {
                errors.add(fieldError.getField() + " " + fieldError.getDefaultMessage());
            }
        }
        if (ex instanceof ConstraintViolationException constraintViolationException) {
            errorCode = ErrorMessage.ErrorCode.InvalidInput.name();
            int errorCount = constraintViolationException.getConstraintViolations().size();
            messageStr = "Validation failed. " + errorCount + " error(s)";
            for (ConstraintViolation<?> violation : constraintViolationException.getConstraintViolations()) {
                errors.add(violation.getPropertyPath() + " " + violation.getMessage());
            }
        }
        if (StringUtils.isBlank(messageStr)) {
            messageStr = ex.getLocalizedMessage();
        }
        return ErrorMessage.builder()
                .code(errorCode)
                .message(ErrorMessage.Message.of(messageStr, request.getRequestURI()))
                .errors(errors)
                .build();
    }

    /**
     * 401错误
     */
    @ExceptionHandler({InvalidCredentialException.class})
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    protected ErrorMessage handle401UnauthorizedException(Exception ex, HttpServletRequest request) {
        String messageStr = ex.getMessage();
        if (StringUtils.isBlank(messageStr)) {
            messageStr = "认证失败";
        }
        return ErrorMessage.builder()
                .code(ErrorMessage.ErrorCode.InvalidAuthenticationInfo.name())
                .message(ErrorMessage.Message.of(messageStr, request.getRequestURI()))
                .build();
    }

    /**
     * 404错误
     */
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class, ResourceNotFoundException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    protected ErrorMessage handle404NotFoundException(Exception ex, HttpServletRequest request) {
        String messageStr = Optional.ofNullable(ex.getMessage()).orElse("The specified resource does not exist");
        return ErrorMessage.builder()
                .code(ErrorMessage.ErrorCode.ResourceNotFound.name())
                .message(ErrorMessage.Message.of(messageStr, request.getRequestURI()))
                .build();
    }

    /**
     * 405错误
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorMessage handle405MethodNotAllowed(Exception ex, HttpServletRequest request) {
        String messageStr = ex.getMessage();
        return ErrorMessage.builder()
                .code(ErrorMessage.ErrorCode.UnsupportedHttpVerb.name())
                .message(ErrorMessage.Message.of(messageStr, request.getRequestURI()))
                .build();
    }

    /**
     * 500错误
     */
    @ExceptionHandler({HttpMessageNotWritableException.class})
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handle500InternalServerError(Exception ex, HttpServletRequest request) {
        String messageStr = ex.getMessage();
        return ErrorMessage.builder()
                .code(ErrorMessage.ErrorCode.InternalError.name())
                .message(ErrorMessage.Message.of(messageStr, request.getRequestURI()))
                .build();
    }

}
