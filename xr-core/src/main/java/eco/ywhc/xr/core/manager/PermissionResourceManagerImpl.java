package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.model.entity.PermissionResource;
import eco.ywhc.xr.core.mapper.PermissionResourceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionResourceManagerImpl implements PermissionResourceManager {

    private final PermissionResourceMapper permissionResourceMapper;

    @Override
    public List<PermissionResource> findAllEntities() {
        QueryWrapper<PermissionResource> qw = new QueryWrapper<>();
        qw.lambda().eq(PermissionResource::getDeleted, 0);
        return permissionResourceMapper.selectList(qw);
    }

}
