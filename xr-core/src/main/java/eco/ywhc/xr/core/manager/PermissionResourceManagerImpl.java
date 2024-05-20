package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.model.entity.PermissionResource;
import eco.ywhc.xr.core.mapper.PermissionResourceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

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

}
