package eco.ywhc.xr.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class JacksonUtils {

    private final static Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

    public static ObjectMapper getObjectMapper() {
        return builder.build();
    }

}
