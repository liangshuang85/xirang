package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.constant.InstanceRefType;
import eco.ywhc.xr.common.model.dto.req.InstanceRoleLarkMemberReq;
import eco.ywhc.xr.common.model.entity.InstanceRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 实例角色
 */
@Transactional(rollbackFor = {Exception.class})
public interface InstanceRoleManager {

    /**
     * 配置实例角色和成员的关系
     */
    void configureInstanceRoleMembers(InstanceRefType refType, long refId, List<InstanceRoleLarkMemberReq> reqs);

    /**
     * 查找所有的实例角色
     */
    List<InstanceRole> findAllEntities();

    /**
     * 查找所有的实例角色
     *
     * @param onlyEnabled 只选择启用的实例角色
     */
    List<InstanceRole> findAllEntities(boolean onlyEnabled);

    /**
     * 根据ID查询实例角色
     *
     * @param id 实例角色ID
     */
    InstanceRole findEntityById(long id);

    /**
     * 查找指定实例角色，没有找到则抛出异常
     *
     * @param id 实例角色ID
     */
    InstanceRole mustFindEntityById(long id);

    /**
     * 获取指定实例角色当前被授予的全部权限资源的编码
     *
     * @param id 实例角色ID
     */
    Set<String> listPermissionCodesById(long id);

    /**
     * 检查角色是否已被分配
     *
     * @param id 角色ID
     */
    boolean isInstanceRoleAssigned(long id);

    /**
     * 获取实例角色ID列表中的实例角色当前被授予的全部权限资源的编码
     *
     * @param ids 实例角色ID列表
     */
    Set<String> listPermissionCodes(Collection<Long> ids);

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
     * 列出指定实例的全部实例角色ID
     *
     * @param refId    实例ID
     * @param memberId 实例角色成员的飞书OpenID
     */
    Set<Long> findAllInstanceRoleIdsByRefIdAndMemberId(long refId, String memberId);

    /**
     * 获取实例角色ID列表中的实例角色当前被授予的全部权限资源的编码
     *
     * @param ids 实例角色ID列表
     * @return Key为实例角色ID，Value为实例角色权限编码列表
     */
    Map<Long, Set<String>> listPermissionCodesByIds(Collection<Long> ids);

    /**
     * 列出实例ID列表中实例的全部实例权限编码
     *
     * @param refIds   实例ID列表
     * @param memberId 实例角色成员的飞书OpenID
     * @return Key为实例ID，Value为实例权限编码列表
     */
    Map<Long, Set<String>> listPermissionCodesByRefIdsAndMemberId(Collection<Long> refIds, String memberId);

    /**
     * 列出指定实例的全部实例权限编码
     *
     * @param refId    实例ID
     * @param memberId 实例角色成员的飞书OpenID
     */
    Set<String> listPermissionCodesByRefIdAndMemberId(long refId, String memberId);

    /**
     * 查找实例负责人所归属唯一实例角色
     *
     * @param refType 实例关联类型
     */
    InstanceRole findInstanceRoleForAssignee(InstanceRefType refType);

    /**
     * 检查实例角色是否已分配给指定成员
     *
     * @param instanceRoleId 实例角色ID
     * @param refId          关联对象ID
     * @param memberId       成员在飞书中的ID
     */
    boolean isInstanceRoleAssigned(long instanceRoleId, long refId, String memberId);

    /**
     * 分配实例角色
     *
     * @param instanceRoleId 实例角色ID
     * @param refId          关联对象ID
     * @param refType        关联对象类型
     * @param memberId       成员在飞书中的ID
     */
    void assignInstanceRole(long instanceRoleId, long refId, InstanceRefType refType, String memberId);

    /**
     * 分配实例负责人角色
     *
     * @param refId    关联对象ID
     * @param refType  关联对象类型
     * @param memberId 成员在飞书中的ID
     */
    void assignInstanceRoleToAssignee(long refId, InstanceRefType refType, String memberId);

    /**
     * 重新分配实例负责人角色
     *
     * @param refId    关联对象ID
     * @param refType  关联对象类型
     * @param memberId 成员在飞书中的ID
     */
    void reAssignInstanceRoleToAssignee(long refId, InstanceRefType refType, String memberId);

}
