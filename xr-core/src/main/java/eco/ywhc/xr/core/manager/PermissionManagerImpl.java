package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.converter.PermissionConverter;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.entity.Permission;
import eco.ywhc.xr.core.mapper.PermissionMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;
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
    public List<Permission> findAllEntitiesByPermissionCodes(Collection<String> permissionCodes) {
        if (CollectionUtils.isEmpty(permissionCodes)) {
            return Collections.emptyList();
        }
        QueryWrapper<Permission> qw = new QueryWrapper<>();
        qw.lambda().eq(Permission::getDeleted, 0).in(Permission::getCode, permissionCodes);
        return permissionMapper.selectList(qw);
    }

    @Override
    public List<PermissionRes> findAllByPermissionCodes(Collection<String> permissionCodes) {
        if (CollectionUtils.isEmpty(permissionCodes)) {
            return Collections.emptyList();
        }
        return findAllEntitiesByPermissionCodes(permissionCodes).stream()
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
    public boolean allExist(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            throw new InternalErrorException("权限编码列表错误");
        }
        final List<Permission> foundPermissionCodes = findAllEntitiesByPermissionCodes(Set.copyOf(codes));
        return foundPermissionCodes.size() == codes.size();
    }

}
