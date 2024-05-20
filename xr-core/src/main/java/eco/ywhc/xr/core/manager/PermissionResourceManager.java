package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.entity.PermissionResource;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

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

}
