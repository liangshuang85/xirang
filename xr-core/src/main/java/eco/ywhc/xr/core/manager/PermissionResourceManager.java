package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.entity.PermissionResource;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 权限资源管理
 */
@Transactional(rollbackFor = {Exception.class})
public interface PermissionResourceManager {

    /**
     * 获取全部权限资源
     */
    List<PermissionResource> findAllEntities();

    /**
     * 获取权限资源编码列表中的全部权限资源
     */
    List<PermissionResource> findAllEntitiesByCodes(Collection<String> codes);

    /**
     * 查找指定权限资源
     */
    PermissionResource findEntityById(long id);

    /**
     * 查找指定权限资源，未找到则抛出异常
     */
    PermissionResource mustFoundEntityById(long id);

    /**
     * 查找指定权限资源
     *
     * @param code 权限资源编码
     */
    @Nullable
    PermissionResource findEntityByCode(String code);

    /**
     * 检查列表中的权限资源编码是否都存在，{@code codes}为空时抛出异常
     *
     * @param codes 权限编码列表
     */
    boolean allExists(Collection<String> codes);

    /**
     * 查找权限资源编码列表中的权限资源
     *
     * @param codes 权限资源编码列表
     */
    boolean anyExists(Collection<String> codes);

}
