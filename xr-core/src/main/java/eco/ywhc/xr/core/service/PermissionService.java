package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.req.PermissionReq;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.model.PageableModelSet;

/**
 * 权限
 */
@Transactional(rollbackFor = {Exception.class})
public interface PermissionService {

    /**
     * 创建一个权限
     */
    Long createOne(PermissionReq req);

    /**
     * 列出权限
     */
    PageableModelSet<PermissionRes> findMany();

    /**
     * 获取指定权限
     *
     * @param id 权限ID
     */
    PermissionRes findOne(Long id);

    /**
     * 更新指定权限
     *
     * @param id 权限ID
     */
    int updateOne(Long id, PermissionReq req);

    /**
     * 删除指定权限
     *
     * @param id 权限ID
     */
    int deleteOne(Long id, boolean logical);

}
