package eco.ywhc.xr.rest.admin;

import eco.ywhc.xr.common.model.dto.impexp.PermissionResourceDump;
import eco.ywhc.xr.common.model.dto.req.PermissionResourceReq;
import eco.ywhc.xr.common.model.dto.res.PermissionResourceRes;
import eco.ywhc.xr.core.service.PermissionResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.sugar.crud.model.CreateResult;
import org.sugar.crud.model.OperationResult;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;

/**
 * 权限资源接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminPermissionResourceController {

    private final PermissionResourceService permissionResourceService;

    /**
     * 新增一个权限资源
     */
    @PostMapping("/admin/rest/permissionResources")
    public CreateResult<Long> createOne(@Valid @RequestBody PermissionResourceReq req) {
        long id = permissionResourceService.createOne(req);
        return CreateResult.of(id);
    }

    /**
     * 列出全部权限资源
     */
    @GetMapping("/admin/rest/permissionResources")
    public PageableModelSet<PermissionResourceRes> findMany() {
        return permissionResourceService.findMany();
    }

    /**
     * 获取指定权限资源
     */
    @GetMapping("/admin/rest/permissionResources/{id}")
    public PermissionResourceRes findOne(@PathVariable long id) {
        return permissionResourceService.findOne(id);
    }

    /**
     * 更新指定权限资源
     */
    @PutMapping("/admin/rest/permissionResources/{id}")
    public OperationResult updateOne(@PathVariable long id, @Valid @RequestBody PermissionResourceReq req) {
        int affected = permissionResourceService.updateOne(id, req);
        return OperationResult.of(affected);
    }

    /**
     * 删除指定权限资源
     */
    @DeleteMapping("/admin/rest/permissionResources/{id}")
    public OperationResult deleteOne(@PathVariable long id) {
        int affected = permissionResourceService.deleteOne(id, true);
        return OperationResult.of(affected);
    }

    /**
     * 导出全部非内置权限资源
     */
    @GetMapping("/admin/rest/permissionResources:export")
    public List<PermissionResourceDump> exportAll() {
        return permissionResourceService.exportAll();
    }

    /**
     * 导入非内置权限资源
     */
    @PostMapping("/admin/rest/permissionResources:import")
    public OperationResult importAll(@Valid @RequestBody List<PermissionResourceDump> dumpList) {
        int affected = permissionResourceService.importAll(dumpList);
        return OperationResult.of(affected);
    }

}
