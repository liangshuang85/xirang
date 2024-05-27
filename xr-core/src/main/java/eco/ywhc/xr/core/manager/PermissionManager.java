package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.entity.Permission;
import org.springframework.lang.Nullable;
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
     * 获取全部权限资源编码
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

    /**
     * 获取全部权限
     *
     * @param resourceCode 权限资源编码
     */
    List<Permission> findAllEntitiesByResourceCode(String resourceCode);

    /**
     * 查找指定权限
     */
    Permission findEntityById(long id);

    /**
     * 查找指定权限，未找到则抛出异常
     */
    Permission mustFoundEntityById(long id);

    /**
     * 查找指定权限
     *
     * @param code 权限编码
     */
    @Nullable
    Permission findEntityByCode(String code);

    /**
     * 检查列表中的权限编码是否都存在，{@code codes}为空时抛出异常
     *
     * @param codes 权限编码列表
     */
    boolean allExist(Collection<String> codes);

}
