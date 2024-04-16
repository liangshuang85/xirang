package eco.ywhc.xr.rest.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.time.Year;
import java.time.YearMonth;
import java.util.Optional;

/**
 * Springdoc OpenAPI配置文件
 */
@Configuration
@RequiredArgsConstructor
public class SpringDocConfig {

    static {
        SpringDocUtils.getConfig().addSimpleTypesForParameterObject(Year.class, YearMonth.class);
    }

    @Nullable
    private final BuildProperties buildProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        String title = Optional.ofNullable(buildProperties).map(BuildProperties::getName).orElse("开发版") + "REST API";
        String version = Optional.ofNullable(buildProperties).map(BuildProperties::getVersion).orElse("(dev build)");
        return new OpenAPI()
                .info(new Info().title(title).version(version))
                .components(new Components()
                        .addSecuritySchemes("x-auth-token",
                                new SecurityScheme().type(SecurityScheme.Type.APIKEY).name("x-auth-token")
                                        .in(SecurityScheme.In.HEADER).scheme("x-auth-token")
                                        .description("在请求需要鉴权的接口时需要发送X-Auth-Token标头")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("x-auth-token"));
    }

}
