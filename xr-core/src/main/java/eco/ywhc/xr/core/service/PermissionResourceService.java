package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.req.PermissionResourceReq;
import eco.ywhc.xr.common.model.dto.res.PermissionResourceRes;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.model.PageableModelSet;

/**
 * 权限资源
 */
@Transactional(rollbackFor = {Exception.class})
public interface PermissionResourceService {

    /**
     * 创建一个权限资源
     */
    Long createOne(PermissionResourceReq req);

    /**
     * 列出权限资源
     */
    PageableModelSet<PermissionResourceRes> findMany();

    /**
     * 获取指定权限资源
     *
     * @param id 权限资源ID
     */
    PermissionResourceRes findOne(Long id);

    /**
     * 更新指定权限资源
     *
     * @param id 权限资源ID
     */
    int updateOne(Long id, PermissionResourceReq req);

    /**
     * 删除指定权限资源
     *
     * @param id 权限资源ID
     */
    int deleteOne(Long id, boolean logical);

}
