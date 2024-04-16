package eco.ywhc.xr.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据字典条目常量注解
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DictionaryEntryConstant {

    /**
     * 标识被注解的类型或者字段是否为数据字典条目；默认为{@link Boolean#TRUE}
     */
    boolean value() default true;

}
