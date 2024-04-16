package eco.ywhc.xr.core.config.property;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Mybatis plus ID生成器相关的属性
 */
@Getter
@Setter
@Configuration
@Validated
@ConfigurationProperties(prefix = "vendor.id-generator")
public class IdGeneratorProperties {

    @NotNull
    @Min(1)
    @Max(31)
    private Integer workerId = 1;

    @NotNull
    @Min(1)
    @Max(31)
    private Integer datacenterId = 1;

}
