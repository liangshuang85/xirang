package eco.ywhc.xr.common.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

/**
 * 换取Token的请求
 */
@Getter
@ToString
public class ExchangeTokenRequest {

    /**
     * 授权码
     */
    @NotBlank
    private String code;

    /**
     * {@code state}参数
     */
    @NotBlank
    private String state;

}
