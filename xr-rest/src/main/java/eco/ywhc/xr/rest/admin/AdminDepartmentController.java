package eco.ywhc.xr.rest.admin;

import eco.ywhc.xr.common.constant.RoleMemberType;
import eco.ywhc.xr.common.model.dto.req.DepartmentRoleConfigureReq;
import eco.ywhc.xr.common.model.dto.res.LarkDepartmentRes;
import eco.ywhc.xr.common.model.dto.res.RoleRes;
import eco.ywhc.xr.core.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.sugar.crud.model.OperationResult;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;
import java.util.Map;

/**
 * 部门接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminDepartmentController {

    private final DepartmentService departmentService;

    /**
     * 获取所有部门
     */
    @GetMapping("/admin/rest/departments")
    public PageableModelSet<LarkDepartmentRes> findMany() {
        return departmentService.findMany();
    }

    /**
     * 获取指定部门
     *
     * @param id 部门ID
     */
    @GetMapping("/admin/rest/departments/{id}")
    public LarkDepartmentRes findOne(@PathVariable String id) {
        return departmentService.findOne(id);
    }

    /**
     * 获取指定部门的角色
     *
     * @param id 部门ID
     */
    @GetMapping("/admin/rest/departments/{id}/roles")
    public Map<RoleMemberType, List<RoleRes>> listRoles(@PathVariable String id) {
        return departmentService.listRoles(id);
    }

    /**
     * 配置指定部门的角色
     *
     * @param id 部门ID
     */
    @PutMapping("/admin/rest/departments/{id}/roles")
    public OperationResult configureRoles(@PathVariable String id, @Valid @RequestBody DepartmentRoleConfigureReq req) {
        departmentService.configureRoles(id, req.getRoles());
        return OperationResult.of(1);
    }

}
