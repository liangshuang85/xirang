package eco.ywhc.xr.rest.admin;

import eco.ywhc.xr.common.model.dto.req.PermissionConfigureReq;
import eco.ywhc.xr.common.model.dto.req.RoleReq;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.dto.res.RoleRes;
import eco.ywhc.xr.common.model.query.RoleQuery;
import eco.ywhc.xr.core.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.sugar.crud.model.CreateResult;
import org.sugar.crud.model.OperationResult;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;

/**
 * 角色接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 创建一个角色
     */
    @PostMapping("/admin/rest/roles")
    public CreateResult<Long> createOne(@Valid @RequestBody RoleReq req) {
        long id = roleService.createOne(req);
        return CreateResult.of(id);
    }

    /**
     * 列出全部角色
     */
    @GetMapping("/admin/rest/roles")
    public PageableModelSet<RoleRes> findMany(@ParameterObject @Valid RoleQuery query) {
        return roleService.findMany(query);
    }

    /**
     * 获取指定角色
     */
    @GetMapping("/admin/rest/roles/{id}")
    public RoleRes findOne(@PathVariable long id) {
        return roleService.findOne(id);
    }

    /**
     * 获取指定角色的权限
     */
    @GetMapping("/admin/rest/roles/{id}/permissions")
    public PageableModelSet<PermissionRes> listPermissions(@PathVariable long id) {
        List<PermissionRes> permissions = roleService.listPermissions(id);
        return PageableModelSet.of(permissions);
    }

    /**
     * 更新指定角色
     */
    @PutMapping("/admin/rest/roles/{id}")
    public OperationResult updateOne(@PathVariable long id, @Valid @RequestBody RoleReq req) {
        int affected = roleService.updateOne(id, req);
        return OperationResult.of(affected);
    }

    /**
     * 配置指定角色的权限
     */
    @PutMapping("/admin/rest/roles/{id}/permissions")
    public OperationResult configurePermissions(@PathVariable long id, @Valid @RequestBody PermissionConfigureReq req) {
        roleService.configurePermissions(id, req.getPermissionCodes());
        return OperationResult.of(1);
    }

    /**
     * 删除指定角色
     */
    @DeleteMapping("/admin/rest/roles/{id}")
    public OperationResult deleteOne(@PathVariable long id) {
        int affected = roleService.logicDeleteOne(id);
        return OperationResult.of(affected);
    }

    /**
     * 禁用指定角色
     */
    @PostMapping("/admin/rest/roles/{id}:disable")
    public OperationResult disable(@PathVariable long id) {
        roleService.disable(id);
        return OperationResult.of(1);
    }

    /**
     * 启用指定角色
     */
    @PostMapping("/admin/rest/roles/{id}:enable")
    public OperationResult enable(@PathVariable long id) {
        roleService.enable(id);
        return OperationResult.of(1);
    }

}
