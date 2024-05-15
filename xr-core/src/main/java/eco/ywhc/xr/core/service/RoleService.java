package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.req.RoleReq;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.dto.res.RoleRes;
import eco.ywhc.xr.common.model.entity.Role;
import eco.ywhc.xr.common.model.query.RoleQuery;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.model.PageableModelSet;
import org.sugar.crud.service.BaseService;

import java.util.Collection;
import java.util.List;

/**
 * 角色
 */
@Transactional(rollbackFor = {Exception.class})
public interface RoleService extends BaseService<Long, Role, RoleReq, RoleRes, RoleQuery> {

    /**
     * 创建一个角色
     */
    @Override
    Long createOne(@NonNull RoleReq req);

    /**
     * 获取所有角色
     */
    @Override
    PageableModelSet<RoleRes> findMany(@NonNull RoleQuery query);

    /**
     * 获取指定角色
     *
     * @param id 角色ID
     */
    @Override
    RoleRes findOne(@NonNull Long id);

    /**
     * 更新指定角色
     */
    @Override
    int updateOne(@NonNull Long id, @NonNull RoleReq req);

    /**
     * 删除一个角色
     *
     * @param id 角色ID
     */
    @Override
    int deleteOne(@NonNull Long id, boolean logical);

    /**
     * 启用一个角色
     *
     * @param id 角色ID
     */
    void enable(long id);

    /**
     * 禁用一个角色
     *
     * @param id 角色ID
     */
    void disable(long id);

    /**
     * 设置角色的权限
     *
     * @param id              角色ID
     * @param permissionCodes 权限编码集合
     */
    void configurePermissions(long id, Collection<String> permissionCodes);

    /**
     * 获取指定角色的权限
     *
     * @param id 角色ID
     */
    List<PermissionRes> listPermissions(long id);

}
