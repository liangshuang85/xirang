package eco.ywhc.xr.common.exception;

import java.io.Serial;

/**
 * 访问被拒绝异常
 */
public class AccessDeniedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2288192140304139117L;

    public AccessDeniedException(String msg) {
        super(msg);
    }

    public AccessDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
