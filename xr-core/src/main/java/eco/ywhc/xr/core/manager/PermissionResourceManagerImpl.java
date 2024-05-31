package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.model.entity.PermissionResource;
import eco.ywhc.xr.core.mapper.PermissionResourceMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.ResourceNotFoundException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PermissionResourceManagerImpl implements PermissionResourceManager {

    private final PermissionResourceMapper permissionResourceMapper;

    @Override
    public List<PermissionResource> findAllEntities() {
        QueryWrapper<PermissionResource> qw = new QueryWrapper<>();
        qw.lambda().eq(PermissionResource::getDeleted, 0).orderByAsc(PermissionResource::getCode);
        return permissionResourceMapper.selectList(qw);
    }

    @Override
    public List<PermissionResource> findAllEntitiesByCodes(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }
        QueryWrapper<PermissionResource> qw = new QueryWrapper<>();
        qw.lambda().eq(PermissionResource::getDeleted, 0).in(PermissionResource::getCode, codes);
        return permissionResourceMapper.selectList(qw);
    }

    @Override
    public PermissionResource findEntityById(long id) {
        return permissionResourceMapper.findEntityById(id);
    }

    @Override
    public PermissionResource mustFoundEntityById(long id) {
        PermissionResource permissionResource = findEntityById(id);
        return Optional.ofNullable(permissionResource).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public PermissionResource findEntityByCode(String code) {
        QueryWrapper<PermissionResource> qw = new QueryWrapper<>();
        qw.lambda().eq(PermissionResource::getDeleted, 0)
                .eq(PermissionResource::getCode, code);
        return permissionResourceMapper.selectOne(qw);
    }

    @Override
    public boolean allExists(Collection<String> codes) {
        Validate.notEmpty(codes, "权限资源编码列表不能为空");
        final List<PermissionResource> foundPermissionResources = findAllEntitiesByCodes(Set.copyOf(codes));
        return foundPermissionResources.size() == codes.size();
    }

    @Override
    public boolean anyExists(Collection<String> codes) {
        Validate.notEmpty(codes, "权限资源编码列表不能为空");
        QueryWrapper<PermissionResource> qw = new QueryWrapper<>();
        qw.lambda().eq(PermissionResource::getDeleted, 0)
                .in(PermissionResource::getCode, codes);
        return permissionResourceMapper.exists(qw);
    }

}
