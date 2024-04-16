package eco.ywhc.xr.core.config.property;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * 飞书相关配置属性
 */
@Getter
@Setter
@ToString
@Validated
@Configuration
@ConfigurationProperties(prefix = "vendor.lark")
public class LarkProperties {

    /**
     * 飞书应用唯一标识
     */
    @NotBlank
    private String appId = "";

    /**
     * 飞书应用秘钥
     */
    @NotBlank
    private String appSecret = "";

}
