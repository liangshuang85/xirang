package eco.ywhc.xr.common.security;

import java.io.Serial;
import java.io.Serializable;

/**
 * 安全上下文
 */
public class SecurityContext implements Serializable {

    @Serial
    private static final long serialVersionUID = 873639579860185875L;

    private CurrentUser currentUser;

    public SecurityContext() {
    }

    public SecurityContext(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    public CurrentUser getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

}
