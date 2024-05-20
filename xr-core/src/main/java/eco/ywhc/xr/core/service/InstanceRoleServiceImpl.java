package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.converter.InstanceRoleConverter;
import eco.ywhc.xr.common.model.dto.req.InstanceRoleReq;
import eco.ywhc.xr.common.model.dto.res.InstanceRoleRes;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.entity.InstanceRole;
import eco.ywhc.xr.common.model.query.InstanceRoleQuery;
import eco.ywhc.xr.core.manager.InstanceRoleManager;
import eco.ywhc.xr.core.manager.PermissionManager;
import eco.ywhc.xr.core.mapper.InstanceRoleMapper;
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
public class InstanceRoleServiceImpl implements InstanceRoleService {

    private final InstanceRoleConverter instanceRoleConverter;

    private final InstanceRoleManager instanceRoleManager;

    private final InstanceRoleMapper instanceRoleMapper;

    private final PermissionManager permissionManager;

    @Override
    public Long createOne(@NonNull InstanceRoleReq req) {
        var entity = instanceRoleConverter.fromRequest(req);
        instanceRoleMapper.insert(entity);
        long id = entity.getId();
        configurePermissionsInternal(id, req.getPermissionCodes());
        return id;
    }

    @Override
    public PageableModelSet<InstanceRoleRes> findMany(@NonNull InstanceRoleQuery query) {
        QueryWrapper<InstanceRole> qw = new QueryWrapper<>();
        qw.lambda().eq(InstanceRole::getDeleted, 0)
                .eq(query.getInstanceRefType() != null, InstanceRole::getRefType, query.getInstanceRefType())
                .eq(query.getEnabled() != null, InstanceRole::getEnabled, query.getEnabled())
                .orderByAsc(InstanceRole::getId);
        var rst = instanceRoleMapper.selectPage(query.paging(true), qw)
                .convert(instanceRoleConverter::toResponse);
        if (query.isNoPaging()) {
            return PageableModelSet.of(rst.getRecords());
        }
        return PageableModelSet.from(rst);
    }

    @Override
    public InstanceRoleRes findOne(@NonNull Long id) {
        var entity = instanceRoleManager.mustFindEntityById(id);
        var response = instanceRoleConverter.toResponse(entity);
        List<PermissionRes> permissions = listPermissions(id);
        response.setPermissions(permissions);
        return response;
    }

    @Override
    public int updateOne(@NonNull Long id, @NonNull InstanceRoleReq req) {
        var entity = instanceRoleManager.mustFindEntityById(id);
        if (entity.getBuiltIn()) {
            throw new InvalidInputException("不能修改内置实例角色");
        }
        if (!entity.getRefType().equals(req.getRefType())) {
            throw new InvalidInputException("不能修改实例角色的实例类型");
        }
        instanceRoleConverter.update(req, entity);
        int affected = instanceRoleMapper.updateById(entity);
        configurePermissionsInternal(entity.getId(), req.getPermissionCodes());
        return affected;
    }

    @Override
    public int deleteOne(@NonNull Long id, boolean logical) {
        var entity = instanceRoleManager.mustFindEntityById(id);
        if (entity.getBuiltIn()) {
            throw new InvalidInputException("不能删除内置实例角色");
        }
        // 检查实例角色是否被赋予给用户
        if (instanceRoleManager.isInstanceRoleAssigned(id)) {
            throw new ConditionNotMetException("该实例角色已经分配给用户，不能直接删除");
        }
        // 删除实例角色
        return instanceRoleMapper.deleteEntityById(id, logical);
    }

    @Override
    public void enable(long id) {
        InstanceRole instanceRole = instanceRoleManager.mustFindEntityById(id);
        if (Boolean.TRUE.equals(instanceRole.getEnabled())) {
            return;
        }
        UpdateWrapper<InstanceRole> uw = new UpdateWrapper<>();
        uw.lambda().eq(InstanceRole::getId, id).set(InstanceRole::getEnabled, true);
        instanceRoleMapper.update(uw);
    }

    @Override
    public void disable(long id) {
        InstanceRole instanceRole = instanceRoleManager.mustFindEntityById(id);
        if (Boolean.FALSE.equals(instanceRole.getEnabled())) {
            return;
        }
        UpdateWrapper<InstanceRole> uw = new UpdateWrapper<>();
        uw.lambda().eq(InstanceRole::getId, id).set(InstanceRole::getEnabled, false);
        instanceRoleMapper.update(uw);
    }

    @Override
    public void configurePermissions(long id, Collection<String> permissionCodes) {
        instanceRoleManager.mustFindEntityById(id);
        configurePermissionsInternal(id, permissionCodes);
    }

    @Override
    public List<PermissionRes> listPermissions(long id) {
        Set<String> currentPermissionCodes = instanceRoleManager.listPermissionCodesById(id);
        return permissionManager.findAllByPermissionCodes(currentPermissionCodes);
    }

    private void validatePermissionCodes(Collection<String> permissionCodes) {
        // 检查权限编码集合是否合法
        if (CollectionUtils.isEmpty(permissionCodes)) {
            return;
        }
        Set<String> allPermissionCodes = permissionManager.findAllPermissionCodes();
        if (!allPermissionCodes.containsAll(permissionCodes)) {
            throw new InvalidInputException("权限编码列表错误");
        }
    }

    private void configurePermissionsInternal(long id, Collection<String> permissionCodes) {
        validatePermissionCodes(permissionCodes);
        Set<String> currentPermissionCodes = instanceRoleManager.listPermissionCodesById(id);
        // 撤销
        Set<String> pendingToRevoke = new HashSet<>(currentPermissionCodes);
        pendingToRevoke.removeAll(permissionCodes);
        if (CollectionUtils.isNotEmpty(pendingToRevoke)) {
            instanceRoleManager.revokePermissions(id, pendingToRevoke);
        }
        // 授予
        if (CollectionUtils.isNotEmpty(permissionCodes)) {
            instanceRoleManager.grantPermissions(id, permissionCodes);
        }
    }

}
