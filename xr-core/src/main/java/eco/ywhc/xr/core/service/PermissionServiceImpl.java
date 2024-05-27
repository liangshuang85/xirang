package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.converter.PermissionConverter;
import eco.ywhc.xr.common.model.dto.req.PermissionReq;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.entity.Permission;
import eco.ywhc.xr.common.model.entity.PermissionResource;
import eco.ywhc.xr.core.manager.PermissionAssignmentManager;
import eco.ywhc.xr.core.manager.PermissionManager;
import eco.ywhc.xr.core.manager.PermissionResourceManager;
import eco.ywhc.xr.core.mapper.PermissionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.ConditionNotMetException;
import org.sugar.commons.exception.InvalidInputException;
import org.sugar.commons.exception.UniqueViolationException;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private final PermissionAssignmentManager permissionAssignmentManager;

    private final PermissionConverter permissionConverter;

    private final PermissionMapper permissionMapper;

    private final PermissionManager permissionManager;

    private final PermissionResourceManager permissionResourceManager;

    @Override
    public Long createOne(PermissionReq req) {
        validateRequest(req, null);
        Permission permission = permissionConverter.fromRequest(req);
        permissionMapper.insert(permission);
        return permission.getId();
    }

    @Override
    public PageableModelSet<PermissionRes> findMany() {
        // 权限资源Map
        Map<String, String> permissionResourceMap = permissionResourceManager.findAllEntities().stream().
                collect(Collectors.toMap(PermissionResource::getCode, PermissionResource::getName));

        QueryWrapper<Permission> qw = new QueryWrapper<>();
        qw.lambda().orderByAsc(Permission::getResourceCode).orderByAsc(Permission::getId);
        List<PermissionRes> rows = permissionMapper.selectList(qw).stream()
                .map(i -> {
                    PermissionRes res = permissionConverter.toResponse(i);
                    String permissionResourceName = permissionResourceMap.getOrDefault(i.getResourceCode(), "");
                    res.setResourceName(permissionResourceName);
                    return res;
                })
                .toList();
        return PageableModelSet.of(rows);
    }

    @Override
    public PermissionRes findOne(Long id) {
        Permission permission = permissionManager.mustFoundEntityById(id);
        return permissionConverter.toResponse(permission);
    }

    @Override
    public int updateOne(Long id, PermissionReq req) {
        validateRequest(req, id);
        Permission permission = permissionManager.mustFoundEntityById(id);
        if (permission.getBuiltIn()) {
            throw new InvalidInputException("不能修改内置权限");
        }
        permissionConverter.update(req, permission);
        return permissionMapper.updateById(permission);
    }

    @Override
    public int deleteOne(Long id, boolean logical) {
        Permission permission = permissionManager.mustFoundEntityById(id);
        if (permission.getBuiltIn()) {
            throw new InvalidInputException("不能删除内置权限");
        }
        List<Long> roleIds = permissionAssignmentManager.listAllSubjectIdsByPermissionCode(permission.getCode());
        if (CollectionUtils.isNotEmpty(roleIds)) {
            throw new ConditionNotMetException("权限仍在使用，不能删除");
        }
        return permissionMapper.deleteEntityById(id, logical);
    }


    private void validateRequest(PermissionReq req, @Nullable Long id) {
        Permission found = permissionManager.findEntityByCode(req.getCode());
        if (found != null && !found.getId().equals(id)) {
            throw new UniqueViolationException("权限编码已被使用");
        }
        PermissionResource permissionResource = permissionResourceManager.findEntityByCode(req.getResourceCode());
        if (permissionResource == null) {
            throw new InvalidInputException("权限资源编码错误");
        }
    }

}
