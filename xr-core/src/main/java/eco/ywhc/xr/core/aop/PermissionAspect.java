package eco.ywhc.xr.core.aop;

import eco.ywhc.xr.common.annotation.Logical;
import eco.ywhc.xr.common.annotation.RequiresPermission;
import eco.ywhc.xr.common.exception.AccessDeniedException;
import eco.ywhc.xr.core.util.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 检查权限的AOP
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PermissionAspect {

    @Pointcut("@annotation(eco.ywhc.xr.common.annotation.RequiresPermission) || " +
            "@within(eco.ywhc.xr.common.annotation.RequiresPermission)")
    private void pointcut() {
    }

    @Around("pointcut()")
    public Object checkPermissions(ProceedingJoinPoint pjp) throws Throwable {
        List<String> permissionCodes = SessionUtils.currentUserPermissionCodes();
        // 检查方法上的权限
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        RequiresPermission methodAnnotation = method.getAnnotation(RequiresPermission.class);
        if (!isPermitted(permissionCodes, methodAnnotation)) {
            throw new AccessDeniedException("当前用户没有足够的权限执行此操作");
        }
        // 检查实例上的权限
        RequiresPermission classAnnotation = pjp.getTarget().getClass().getAnnotation(RequiresPermission.class);
        if (!isPermitted(permissionCodes, classAnnotation)) {
            throw new AccessDeniedException("当前用户没有足够的权限执行此操作");
        }
        return pjp.proceed();
    }

    private boolean isPermitted(Collection<String> permissionCodes, RequiresPermission annotation) {
        if (annotation == null || annotation.value().length == 0) {
            return true;
        }
        if (annotation.logical() == Logical.AND) {
            return CollectionUtils.containsAll(permissionCodes, Arrays.asList(annotation.value()));
        } else {
            return CollectionUtils.containsAny(permissionCodes, Arrays.asList(annotation.value()));
        }
    }

}
