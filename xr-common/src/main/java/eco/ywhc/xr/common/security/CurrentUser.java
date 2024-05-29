package eco.ywhc.xr.common.security;

import lombok.Builder;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * 当前会话用户
 */
@Builder(toBuilder = true)
@Value
public class CurrentUser implements Serializable {

    @Serial
    private static final long serialVersionUID = -1263135012306586601L;

    /**
     * 用户ID
     */
    Long id;

    /**
     * 用户名
     */
    String username;

    /**
     * 昵称
     */
    String nickname;

    /**
     * 全名
     */
    String fullName;

    /**
     * 电话号码
     */
    String phoneNumber;

    /**
     * Email
     */
    String email;

    /**
     * 是否为管理员
     */
    Boolean admin;

    /**
     * 封禁标记
     */
    Boolean blocked;

    /**
     * 飞书用户的OpenID
     */
    String larkOpenId;

    /**
     * 权限编码集合
     */
    Set<String> permissionCodes;

}
