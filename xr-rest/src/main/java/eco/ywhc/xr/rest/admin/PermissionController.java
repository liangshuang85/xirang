package eco.ywhc.xr.rest.admin;

import eco.ywhc.xr.common.model.dto.req.PermissionReq;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.core.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.sugar.crud.model.CreateResult;
import org.sugar.crud.model.OperationResult;
import org.sugar.crud.model.PageableModelSet;

/**
 * 权限接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 新增一个权限
     */
    @PostMapping("/admin/rest/permissions")
    public CreateResult<Long> createOne(@Valid @RequestBody PermissionReq req) {
        long id = permissionService.createOne(req);
        return CreateResult.of(id);
    }

    /**
     * 列出全部权限
     */
    @GetMapping("/admin/rest/permissions")
    public PageableModelSet<PermissionRes> findMany() {
        return permissionService.findMany();
    }

    /**
     * 获取指定权限
     */
    @GetMapping("/admin/rest/permissions/{id}")
    public PermissionRes findOne(@PathVariable long id) {
        return permissionService.findOne(id);
    }

    /**
     * 更新指定权限
     */
    @PutMapping("/admin/rest/permissions/{id}")
    public OperationResult updateOne(@PathVariable long id, @Valid @RequestBody PermissionReq req) {
        int affected = permissionService.updateOne(id, req);
        return OperationResult.of(affected);
    }

    /**
     * 删除指定权限
     */
    @DeleteMapping("/admin/rest/permissions/{id}")
    public OperationResult deleteOne(@PathVariable long id) {
        int affected = permissionService.deleteOne(id, true);
        return OperationResult.of(affected);
    }

}
