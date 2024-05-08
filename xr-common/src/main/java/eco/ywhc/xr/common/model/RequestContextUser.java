package eco.ywhc.xr.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 会话用户
 */
@Getter
@Setter
@ToString
public class RequestContextUser implements Serializable {

    @Serial
    private static final long serialVersionUID = -1263135012306586601L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 全名
     */
    private String fullName;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * Email
     */
    private String email;

    /**
     * 是否为管理员
     */
    private Boolean admin;

    /**
     * 封禁标记
     */
    private Boolean blocked;

    /**
     * 飞书用户的OpenID
     */
    private String larkOpenId;

}
