package eco.ywhc.xr.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要指定权限
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {

    /**
     * 用以确定是否允许调用受此注解保护的代码的权限字符串
     */
    String[] value() default {};

    /**
     * 用于权限检查中出现多个权限标识时的逻辑运算符。默认为“与”
     */
    Logical logical() default Logical.AND;

}
