package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.constant.RoleMemberType;
import eco.ywhc.xr.common.model.dto.res.LarkDepartmentRes;
import eco.ywhc.xr.common.model.dto.res.RoleRes;
import eco.ywhc.xr.common.model.entity.LarkDepartment;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 部门
 */
@Transactional(rollbackFor = {Exception.class})
public interface DepartmentService {

    /**
     * 获取所有部门
     */
    PageableModelSet<LarkDepartmentRes> findMany();

    /**
     * 查找指定部门
     *
     * @param id 部门ID
     */
    LarkDepartment findEntityById(String id);

    /**
     * 查找指定部门，没有找到则抛出异常
     *
     * @param id 部门ID
     */
    LarkDepartment mustFindEntityById(String id);

    /**
     * 查找指定部门
     *
     * @param id 部门ID
     */
    LarkDepartmentRes findOne(String id);

    /**
     * 配置部门的角色
     *
     * @param id    部门ID
     * @param roles 角色成员类型-角色ID集合Map
     */
    void configureRoles(String id, Map<RoleMemberType, Set<Long>> roles);

    /**
     * 获取指定部门的角色
     *
     * @param id 部门ID
     */
    Map<RoleMemberType, List<RoleRes>> listRoles(String id);

}
