package eco.ywhc.xr.core.manager.lark;

import eco.ywhc.xr.common.model.dto.res.DepartmentRes;
import eco.ywhc.xr.common.model.entity.LarkDepartment;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 同步飞书部门信息
     */
    void syncLarkDepartments();

    /**
     * 获取飞书部门信息
     *
     * @param pageToken 分页标记
     */
    List<LarkDepartment> listLarkDepartments(String pageToken);

}
