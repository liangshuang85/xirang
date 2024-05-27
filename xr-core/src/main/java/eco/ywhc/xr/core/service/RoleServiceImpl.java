package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.converter.RoleConverter;
import eco.ywhc.xr.common.model.dto.req.RoleReq;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.dto.res.RoleRes;
import eco.ywhc.xr.common.model.entity.Role;
import eco.ywhc.xr.common.model.query.RoleQuery;
import eco.ywhc.xr.core.manager.PermissionManager;
import eco.ywhc.xr.core.manager.RoleManager;
import eco.ywhc.xr.core.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.ConditionNotMetException;
import org.sugar.commons.exception.InvalidInputException;
import org.sugar.crud.model.PageableModelSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleConverter roleConverter;

    private final RoleManager roleManager;

    private final RoleMapper roleMapper;

    private final PermissionManager permissionManager;

    @Override
    public Long createOne(@NonNull RoleReq req) {
        var entity = roleConverter.fromRequest(req);
        roleMapper.insert(entity);
        long id = entity.getId();
        configurePermissionsInternal(id, req.getPermissionCodes());
        return id;
    }

    @Override
    public PageableModelSet<RoleRes> findMany(@NonNull RoleQuery query) {
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw.lambda().eq(Role::getDeleted, 0)
                .eq(query.getEnabled() != null, Role::getEnabled, query.getEnabled())
                .eq(query.getIncludeBasic() != null, Role::getBasic, query.getIncludeBasic())
                .orderByAsc(Role::getId);
        var rst = roleMapper.selectPage(query.paging(true), qw)
                .convert(roleConverter::toResponse);
        if (query.isNoPaging()) {
            return PageableModelSet.of(rst.getRecords());
        }
        return PageableModelSet.from(rst);
    }

    @Override
    public RoleRes findOne(@NonNull Long id) {
        var entity = roleManager.mustFindEntityById(id);
        var response = roleConverter.toResponse(entity);
        List<PermissionRes> permissions = listPermissions(id);
        response.setPermissions(permissions);
        return response;
    }

    @Override
    public int updateOne(@NonNull Long id, @NonNull RoleReq req) {
        var entity = roleManager.mustFindEntityById(id);
        if (entity.getBuiltIn()) {
            throw new InvalidInputException("不能修改内置角色");
        }
        roleConverter.update(req, entity);
        int affected = roleMapper.updateById(entity);
        configurePermissionsInternal(entity.getId(), req.getPermissionCodes());
        return affected;
    }

    @Override
    public int deleteOne(@NonNull Long id, boolean logical) {
        var entity = roleManager.mustFindEntityById(id);
        if (entity.getBuiltIn()) {
            throw new InvalidInputException("不能删除内置角色");
        }
        // 检查角色是否被赋予给用户
        if (roleManager.isRoleAssigned(id)) {
            throw new ConditionNotMetException("该角色已经分配给用户，不能直接删除");
        }
        // 删除角色
        return roleMapper.deleteEntityById(id, logical);
    }

    @Override
    public void enable(long id) {
        Role role = roleManager.mustFindEntityById(id);
        if (Boolean.TRUE.equals(role.getEnabled())) {
            return;
        }
        UpdateWrapper<Role> uw = new UpdateWrapper<>();
        uw.lambda().eq(Role::getId, id).set(Role::getEnabled, true);
        roleMapper.update(uw);
    }

    @Override
    public void disable(long id) {
        Role role = roleManager.mustFindEntityById(id);
        if (Boolean.FALSE.equals(role.getEnabled())) {
            return;
        }
        UpdateWrapper<Role> uw = new UpdateWrapper<>();
        uw.lambda().eq(Role::getId, id).set(Role::getEnabled, false);
        roleMapper.update(uw);
    }

    @Override
    public void configurePermissions(long id, Collection<String> permissionCodes) {
        roleManager.mustFindEntityById(id);
        configurePermissionsInternal(id, permissionCodes);
    }

    @Override
    public List<PermissionRes> listPermissions(long id) {
        Set<String> currentPermissionCodes = roleManager.findCurrentPermissionCodes(id);
        return permissionManager.findAllByPermissionCodes(currentPermissionCodes);
    }

    private void validatePermissionCodes(Collection<String> permissionCodes) {
        // 检查权限编码集合是否合法
        if (CollectionUtils.isEmpty(permissionCodes)) {
            return;
        }
        if (!permissionManager.allExist(permissionCodes)) {
            throw new InvalidInputException("权限编码列表错误");
        }
    }

    private void configurePermissionsInternal(long id, Collection<String> permissionCodes) {
        validatePermissionCodes(permissionCodes);
        Set<String> currentPermissionCodes = roleManager.findCurrentPermissionCodes(id);
        // 撤销
        Set<String> pendingToRevoke = new HashSet<>(currentPermissionCodes);
        pendingToRevoke.removeAll(permissionCodes);
        if (CollectionUtils.isNotEmpty(pendingToRevoke)) {
            roleManager.revokePermissions(id, pendingToRevoke);
        }
        // 授予
        if (CollectionUtils.isNotEmpty(permissionCodes)) {
            roleManager.grantPermissions(id, permissionCodes);
        }
    }

}
