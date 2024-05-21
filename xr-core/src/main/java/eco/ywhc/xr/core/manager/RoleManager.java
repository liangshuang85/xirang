package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.res.RoleRes;
import eco.ywhc.xr.common.model.entity.Role;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 角色管理
 */
@Transactional(rollbackFor = {Exception.class})
public interface RoleManager {

    /**
     * 查找ID列表中的角色
     *
     * @param ids 角色ID列表
     */
    List<Role> findAllEntitiesByIds(Collection<Long> ids);

    /**
     * 查找ID列表中的角色
     *
     * @param ids 角色ID列表
     */
    List<RoleRes> findAllByIds(Collection<Long> ids);

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
     * 获取指定角色当前被授予的全部权限资源的编码
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

    /**
     * 列出指定飞书用户所分配的全部角色的ID
     *
     * @param larkUserOpenId 飞书用户的OpenID
     */
    Set<Long> listAssignedRoleIds(String larkUserOpenId);

    /**
     * 列出列表中所有角色所获得的全部权限的权限编码
     *
     * @param ids 角色ID列表
     */
    Set<String> listGrantedPermissionCodes(Collection<Long> ids);

    /**
     * 获取全部角色
     *
     * @param permissionCode 权限编码
     */
    List<Long> findAllEntitiesByPermissionCode(String permissionCode);

}
