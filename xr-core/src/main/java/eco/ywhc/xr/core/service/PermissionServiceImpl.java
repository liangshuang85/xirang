package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.converter.PermissionConverter;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.entity.Permission;
import eco.ywhc.xr.core.mapper.PermissionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private final PermissionConverter permissionConverter;

    private final PermissionMapper permissionMapper;

    @Override
    public PageableModelSet<PermissionRes> findMany() {
        QueryWrapper<Permission> qw = new QueryWrapper<>();
        qw.lambda().orderByAsc(Permission::getResourceCode).orderByAsc(Permission::getId);
        List<PermissionRes> rows = permissionMapper.selectList(qw).stream()
                .map(permissionConverter::toResponse)
                .toList();
        return PageableModelSet.of(rows);
    }

}
