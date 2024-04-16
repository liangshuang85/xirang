package eco.ywhc.xr.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.MDC;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage implements Serializable {

    public static final String DEFAULT_MDC_REQUEST_ID = "Slf4jMDCFilter.UUID";

    @Serial
    private static final long serialVersionUID = -1955008679189640550L;

    private String code;

    private Message message;

    @Builder.Default
    private List<String> errors = new ArrayList<>();

    public enum ErrorCode {
        /**
         * 401错误：无效的输入
         */
        InvalidInput,
        /**
         * 401错误：认证信息无效
         */
        InvalidAuthenticationInfo,
        /**
         * 401错误：未提供授权信息标头
         */
        NoAuthorizationHeader,
        /**
         * 401错误：授权信息无效
         */
        InvalidAuthorizationHeader,
        /**
         * 403错误：当前用户没有足够的权限执行此操作
         */
        InsufficientAccountPermissions,
        /**
         * 404错误：指定的资源不存在
         */
        ResourceNotFound,
        /**
         * 405错误：资源不支持指定的 HTTP 动词
         */
        UnsupportedHttpVerb,
        /**
         * 500错误：服务器遇到内部错误。 请重试请求
         */
        InternalError,
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {

        @Builder.Default
        private String lang = "en-us";

        private String value;

        private String path;

        @Builder.Default
        private String requestId = MDC.get(DEFAULT_MDC_REQUEST_ID);

        @Builder.Default
        private OffsetDateTime timestamp = OffsetDateTime.now();

        public static Message of(String value) {
            return Message.builder().value(value).build();
        }

        public static Message of(String value, String path) {
            return Message.builder().value(value).path(path).build();
        }

    }

}
