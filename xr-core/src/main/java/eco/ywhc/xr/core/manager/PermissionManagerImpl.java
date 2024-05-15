package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.converter.PermissionConverter;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.entity.Permission;
import eco.ywhc.xr.core.mapper.PermissionMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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

}
