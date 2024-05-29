package eco.ywhc.xr.common.security;

import org.springframework.util.Assert;

/**
 * 安全上下文持有者
 */
public class SecurityContextHolder {

    private static final ThreadLocal<SecurityContext> contextHolder = new InheritableThreadLocal<>();

    static {
        initialize();
    }

    private static void initialize() {
        final SecurityContext emptyContext = createEmptyContext();
        setContext(emptyContext);
    }

    private static SecurityContext createEmptyContext() {
        return new SecurityContext();
    }

    /**
     * 获取当前的<code>SecurityContext</code>。
     *
     * @return 安全上下文（永不为<code>null</code>）
     */
    public static SecurityContext getContext() {
        return contextHolder.get();
    }

    /**
     * 将一个新的<code>SecurityContext</code>关联到当前执行的线程。
     *
     * @param context 新的<code>SecurityContext</code>（不能为<code>null</code>）
     */
    public static void setContext(SecurityContext context) {
        Assert.notNull(context, "只允许关联非null的SecurityContext实例");
        contextHolder.set(context);
    }

    public static void clearContext() {
        contextHolder.remove();
    }

}
