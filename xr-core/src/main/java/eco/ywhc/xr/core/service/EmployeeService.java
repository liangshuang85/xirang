package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.res.EmployeeRes;
import eco.ywhc.xr.common.model.dto.res.RoleRes;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;
import java.util.Set;

/**
 * 员工
 */
@Transactional(rollbackFor = {Exception.class})
public interface EmployeeService {

    /**
     * 获取全部员工
     */
    PageableModelSet<EmployeeRes> findMany();

    /**
     * 查找指定员工
     *
     * @param id 员工ID
     */
    EmployeeRes findOne(String id);

    /**
     * 配置员工的角色
     *
     * @param id      员工ID
     * @param roleIds 角色ID列表
     */
    void configureRoles(String id, Set<Long> roleIds);

    /**
     * 获取指定员工的角色
     *
     * @param id 员工ID
     */
    List<RoleRes> listRoles(String id);

}
