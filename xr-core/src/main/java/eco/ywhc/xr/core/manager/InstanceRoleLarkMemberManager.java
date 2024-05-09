package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.res.InstanceRoleLarkMemberRes;
import eco.ywhc.xr.common.model.entity.InstanceRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = {Exception.class})
public interface InstanceRoleLarkMemberManager {

    /**
     * 查询所有启用的实例角色
     */
    List<InstanceRole> findAllInstanceRoles();

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

}
