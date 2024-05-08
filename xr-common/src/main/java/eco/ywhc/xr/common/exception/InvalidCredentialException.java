package eco.ywhc.xr.common.exception;

import java.io.Serial;

/**
 * 凭证不可用
 */
public class InvalidCredentialException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6923885199659050837L;

    public InvalidCredentialException(String msg) {
        super(msg);
    }

    public InvalidCredentialException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
