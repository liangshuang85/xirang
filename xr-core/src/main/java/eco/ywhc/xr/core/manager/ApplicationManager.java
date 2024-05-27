package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.entity.Application;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

/**
 * 应用
 */
@Transactional(rollbackFor = {Exception.class})
public interface ApplicationManager {

    /**
     * 查找指定应用
     *
     * @param id 应用ID
     */
    Application findEntityById(long id);

    /**
     * 查找指定应用
     *
     * @param id 应用ID
     */
    Application mustFoundEntityById(long id);

    /**
     * 获取指定应用当前被授予的全部权限资源的编码
     *
     * @param id 应用ID
     */
    Set<String> findCurrentPermissionCodes(long id);

    /**
     * 授予应用权限
     *
     * @param id              应用ID
     * @param permissionCodes 权限编码集合
     */
    void grantPermissions(long id, Collection<String> permissionCodes);

    /**
     * 撤销应用的授权
     *
     * @param id              应用ID
     * @param permissionCodes 权限编码集合
     */
    void revokePermissions(long id, Collection<String> permissionCodes);

}
