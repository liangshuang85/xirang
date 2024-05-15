package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.entity.Permission;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 权限资源管理
 */
@Transactional(rollbackFor = {Exception.class})
public interface PermissionManager {

    /**
     * 获取全部权限
     */
    List<Permission> findAllEntities();

    /**
     * 获取全部权限资源代码
     */
    Set<String> findAllPermissionCodes();

    /**
     * 获取权限编码列表中的全部权限
     */
    List<Permission> findAllEntitiesByPermissionCodes(Collection<String> permissionCodes);

    /**
     * 获取权限编码列表中的全部权限
     */
    List<PermissionRes> findAllByPermissionCodes(Collection<String> permissionCodes);

}
