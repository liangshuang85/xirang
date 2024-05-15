package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.model.PageableModelSet;

/**
 * 权限
 */
@Transactional(rollbackFor = {Exception.class})
public interface PermissionService {

    PageableModelSet<PermissionRes> findMany();

}
