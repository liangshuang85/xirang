package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.entity.PermissionResource;
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

}
