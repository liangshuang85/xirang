package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.res.InstanceRoleLarkMemberRes;
import eco.ywhc.xr.common.model.entity.InstanceRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = {Exception.class})
public interface InstanceRoleLarkMemberManager {

    /**
     * 插入角色成员
     */
    void insertInstanceRoleLarkMember(Object req, long refId);

    /**
     * 删除角色成员
     */
    void deleteInstanceRoleLarkMember(long refId);

    /**
     * 根据关联ID查询角色成员
     *
     * @param refId 关联ID
     */
    List<InstanceRoleLarkMemberRes> findInstanceRoleLarkMemberByRefId(long refId);

    /**
     * 根据关联ID查询角色成员ID
     *
     * @param refId 关联ID
     */
    List<String> getMemberIdsByRefId(long refId);

    /**
     * 根据实例角色ID查询成员ID
     *
     * @param instanceRoleId 实例角色ID
     * @param refId          关联ID
     */
    List<String> getMemberIdsByInstanceRoleIdAndRefId(long instanceRoleId, long refId);

    /**
     * 根据ID查询实例角色
     *
     * @param id 实例角色ID
     */
    InstanceRole findInstanceRoleById(long id);

}
