package eco.ywhc.xr.core.config;

import eco.ywhc.xr.common.dictionary.DictionaryLoader;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DictionaryLoaderConfig {

    @Bean
    public DictionaryLoader dictionaryLoader(MessageSource messageSource) {
        return DictionaryLoader.createWith(messageSource);
    }

}
