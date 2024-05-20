package eco.ywhc.xr.rest.admin;

import eco.ywhc.xr.common.model.dto.req.InstanceRoleReq;
import eco.ywhc.xr.common.model.dto.req.PermissionConfigureReq;
import eco.ywhc.xr.common.model.dto.res.InstanceRoleRes;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.query.InstanceRoleQuery;
import eco.ywhc.xr.core.service.InstanceRoleService;
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
 * 实例角色接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminInstanceRoleController {

    private final InstanceRoleService instanceRoleService;

    /**
     * 创建一个实例角色
     */
    @PostMapping("/admin/rest/instanceRoles")
    public CreateResult<Long> createOne(@Valid @RequestBody InstanceRoleReq req) {
        long id = instanceRoleService.createOne(req);
        return CreateResult.of(id);
    }

    /**
     * 获取实例角色列表，结果以分页或不分页的形式返回
     */
    @GetMapping("/admin/rest/instanceRoles")
    public PageableModelSet<InstanceRoleRes> findMany(@ParameterObject @Valid InstanceRoleQuery query) {
        return instanceRoleService.findMany(query);
    }

    /**
     * 获取指定实例角色
     */
    @GetMapping("/admin/rest/instanceRoles/{id}")
    public InstanceRoleRes findOne(@PathVariable long id) {
        return instanceRoleService.findOne(id);
    }

    /**
     * 获取指定实例角色的权限
     */
    @GetMapping("/admin/rest/instanceRoles/{id}/permissions")
    public PageableModelSet<PermissionRes> listPermissions(@PathVariable long id) {
        List<PermissionRes> permissions = instanceRoleService.listPermissions(id);
        return PageableModelSet.of(permissions);
    }

    /**
     * 更新指定实例角色
     */
    @PutMapping("/admin/rest/instanceRoles/{id}")
    public OperationResult updateOne(@PathVariable long id, @Valid @RequestBody InstanceRoleReq req) {
        int affected = instanceRoleService.updateOne(id, req);
        return OperationResult.of(affected);
    }

    /**
     * 配置指定实例角色的权限
     */
    @PutMapping("/admin/rest/instanceRoles/{id}/permissions")
    public OperationResult configurePermissions(@PathVariable long id, @Valid @RequestBody PermissionConfigureReq req) {
        instanceRoleService.configurePermissions(id, req.getPermissionCodes());
        return OperationResult.of(1);
    }

    /**
     * 删除指定实例角色
     */
    @DeleteMapping("/admin/rest/instanceRoles/{id}")
    public OperationResult deleteOne(@PathVariable long id) {
        int affected = instanceRoleService.logicDeleteOne(id);
        return OperationResult.of(affected);
    }

    /**
     * 禁用指定实例角色
     */
    @PostMapping("/admin/rest/instanceRoles/{id}:disable")
    public OperationResult disable(@PathVariable long id) {
        instanceRoleService.disable(id);
        return OperationResult.of(1);
    }

    /**
     * 启用指定实例角色
     */
    @PostMapping("/admin/rest/instanceRoles/{id}:enable")
    public OperationResult enable(@PathVariable long id) {
        instanceRoleService.enable(id);
        return OperationResult.of(1);
    }

}
