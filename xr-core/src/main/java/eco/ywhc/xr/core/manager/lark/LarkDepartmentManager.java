package eco.ywhc.xr.core.manager.lark;

import eco.ywhc.xr.common.model.dto.res.DepartmentRes;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

/**
 * 飞书部门管理
 */
@Transactional(rollbackFor = {Exception.class})
public interface LarkDepartmentManager {

    /**
     * 根据部门ID从飞书获取部门信息
     *
     * @param departmentId 部门ID
     */
    DepartmentRes getDepartmentByDepartmentId(@NonNull String departmentId);

}
