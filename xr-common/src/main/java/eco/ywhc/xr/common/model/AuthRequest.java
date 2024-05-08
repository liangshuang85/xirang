package eco.ywhc.xr.common.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

/**
 * 认证请求
 */
@Getter
@ToString
public class AuthRequest {

    /**
     * 用户名或者Email地址
     */
    @NotBlank
    private String identity;

    /**
     * 密码
     */
    @NotBlank
    private String password;

}
