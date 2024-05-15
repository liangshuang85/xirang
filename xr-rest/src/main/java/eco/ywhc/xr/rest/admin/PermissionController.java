package eco.ywhc.xr.rest.admin;

import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.core.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
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
     * 列出全部权限
     */
    @GetMapping("/admin/rest/permissions")
    public PageableModelSet<PermissionRes> findMany() {
        return permissionService.findMany();
    }

}
