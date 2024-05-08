package eco.ywhc.xr.common.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 修改密码的Request
 */
@Getter
public class PasswordChangeRequest {

    /**
     * 当前密码
     */
    @NotBlank
    private String currentPassword;

    /**
     * 新密码
     */
    @NotBlank
    private String newPassword;

    /**
     * 确认新密码
     */
    @NotBlank
    private String newPasswordConfirmation;

}
