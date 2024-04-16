package eco.ywhc.xr.core.config;

import com.lark.oapi.Client;
import com.lark.oapi.core.enums.AppType;
import com.lark.oapi.core.enums.BaseUrlEnum;
import eco.ywhc.xr.core.config.property.LarkProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 飞书客户端配置
 */
@Configuration
@RequiredArgsConstructor
public class LarkClientConfig {

    private final LarkProperties larkProperties;

    @Bean
    public Client newLarkClient() {
        return Client.newBuilder(larkProperties.getAppId(), larkProperties.getAppSecret())
                .appType(AppType.SELF_BUILT)
                .openBaseUrl(BaseUrlEnum.FeiShu)
                .requestTimeout(30, TimeUnit.SECONDS)
                .logReqAtDebug(true)
                .build();
    }

}
