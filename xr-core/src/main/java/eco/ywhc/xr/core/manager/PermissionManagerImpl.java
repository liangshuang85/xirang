package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.converter.PermissionConverter;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.entity.Permission;
import eco.ywhc.xr.core.mapper.PermissionMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.ResourceNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionManagerImpl implements PermissionManager {

    private final PermissionConverter permissionConverter;

    private final PermissionMapper permissionMapper;

    @Override
    public List<Permission> findAllEntities() {
        QueryWrapper<Permission> qw = new QueryWrapper<>();
        qw.lambda().eq(Permission::getDeleted, 0);
        return permissionMapper.selectList(qw);
    }

    @Override
    public Set<String> findAllPermissionCodes() {
        return findAllEntities().stream().map(Permission::getCode).collect(Collectors.toSet());
    }

    @Override
    public List<Permission> findAllEntitiesByCodes(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        QueryWrapper<Permission> qw = new QueryWrapper<>();
        qw.lambda().eq(Permission::getDeleted, 0).in(Permission::getCode, codes);
        return permissionMapper.selectList(qw);
    }

    @Override
    public List<PermissionRes> findAllCodes(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        return findAllEntitiesByCodes(codes).stream()
                .map(permissionConverter::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> findAllEntitiesByResourceCode(String resourceCode) {
        if (StringUtils.isBlank(resourceCode)) {
            return Collections.emptyList();
        }
        QueryWrapper<Permission> qw = new QueryWrapper<>();
        qw.lambda().eq(Permission::getDeleted, 0).eq(Permission::getResourceCode, resourceCode);
        return permissionMapper.selectList(qw);
    }

    @Override
    public Permission findEntityById(long id) {
        return permissionMapper.findEntityById(id);
    }

    @Override
    public Permission mustFoundEntityById(long id) {
        Permission permission = findEntityById(id);
        return Optional.ofNullable(permission).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Permission findEntityByCode(String code) {
        QueryWrapper<Permission> qw = new QueryWrapper<>();
        qw.lambda().eq(Permission::getDeleted, 0)
                .eq(Permission::getCode, code);
        return permissionMapper.selectOne(qw);
    }

    @Override
    public boolean allExists(Collection<String> codes) {
        Validate.notEmpty(codes, "权限编码列表不能为空");
        final List<Permission> foundPermissionCodes = findAllEntitiesByCodes(Set.copyOf(codes));
        return foundPermissionCodes.size() == codes.size();
    }

    @Override
    public boolean anyExists(Collection<String> codes) {
        Validate.notEmpty(codes, "权限编码列表不能为空");
        QueryWrapper<Permission> qw = new QueryWrapper<>();
        qw.lambda().eq(Permission::getDeleted, 0)
                .in(Permission::getCode, codes);
        return permissionMapper.exists(qw);
    }

}
