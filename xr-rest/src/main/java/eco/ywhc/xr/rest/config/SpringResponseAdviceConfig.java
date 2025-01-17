package eco.ywhc.xr.rest.config;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.sugar.crud.model.PageableModelSet;

/**
 * 重写 Mybatis Plus 的分页结果
 */
@RestControllerAdvice
public class SpringResponseAdviceConfig implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof IPage<?> page) {
            return PageableModelSet.from(page);
        }
        return body;
    }

}
