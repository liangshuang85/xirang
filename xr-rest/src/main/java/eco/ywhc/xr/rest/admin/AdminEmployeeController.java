package eco.ywhc.xr.rest.admin;

import eco.ywhc.xr.common.constant.RoleMemberType;
import eco.ywhc.xr.common.model.dto.req.EmployeeRoleConfigureReq;
import eco.ywhc.xr.common.model.dto.res.EmployeeRes;
import eco.ywhc.xr.common.model.dto.res.RoleRes;
import eco.ywhc.xr.core.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.sugar.crud.model.OperationResult;
import org.sugar.crud.model.PageableModelSet;

import java.util.HashSet;
import java.util.List;

/**
 * 员工接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminEmployeeController {

    private final EmployeeService employeeService;

    /**
     * 获取所有员工
     */
    @GetMapping("/admin/rest/employees")
    public PageableModelSet<EmployeeRes> findMany() {
        return employeeService.findMany();
    }

    /**
     * 获取指定员工
     *
     * @param id 员工ID
     */
    @GetMapping("/admin/rest/employees/{id}")
    public EmployeeRes findOne(@PathVariable String id) {
        return employeeService.findOne(id);
    }

    /**
     * 获取指定员工的角色
     *
     * @param id 员工ID
     */
    @GetMapping("/admin/rest/employees/{id}/roles")
    public List<RoleRes> listRoles(@PathVariable String id) {
        return employeeService.listRoles(id);
    }

    /**
     * 配置指定员工的角色
     *
     * @param id 员工ID
     */
    @PutMapping("/admin/rest/employees/{id}/roles")
    public OperationResult configureRoles(@PathVariable String id, @Valid @RequestBody EmployeeRoleConfigureReq req) {
        employeeService.configureRoles(id, req.getRoles().getOrDefault(RoleMemberType.EMPLOYEE, new HashSet<>()));
        return OperationResult.of(1);
    }

}
