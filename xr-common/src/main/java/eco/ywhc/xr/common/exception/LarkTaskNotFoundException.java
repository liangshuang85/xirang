package eco.ywhc.xr.common.exception;

import java.io.Serial;

/**
 * 飞书任务未找到异常
 */
public class LarkTaskNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -405783989518411593L;

    public LarkTaskNotFoundException(String msg) {
        super(msg);
    }

    public LarkTaskNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
