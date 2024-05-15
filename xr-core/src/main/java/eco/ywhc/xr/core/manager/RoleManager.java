package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.entity.Role;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

/**
 * 角色管理
 */
@Transactional(rollbackFor = {Exception.class})
public interface RoleManager {

    /**
     * 查找指定角色
     *
     * @param id 角色ID
     */
    Role findEntityById(long id);

    /**
     * 查找指定角色，没有找到则抛出异常
     *
     * @param id 角色ID
     */
    Role mustFindEntityById(long id);

    /**
     * 检查角色是否已被分配
     *
     * @param id 角色ID
     */
    boolean isRoleAssigned(long id);

    /**
     * 获取指定角色当前被授予的全部权限资源代码
     *
     * @param id 角色ID
     */
    Set<String> findCurrentPermissionCodes(long id);

    /**
     * 授予角色权限
     *
     * @param id              角色ID
     * @param permissionCodes 权限编码集合
     */
    void grantPermissions(long id, Collection<String> permissionCodes);

    /**
     * 撤销角色的授权
     *
     * @param id              角色ID
     * @param permissionCodes 权限编码集合
     */
    void revokePermissions(long id, Collection<String> permissionCodes);

}
