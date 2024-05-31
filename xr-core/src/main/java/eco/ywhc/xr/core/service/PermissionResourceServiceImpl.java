package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.converter.PermissionResourceConverter;
import eco.ywhc.xr.common.model.dto.impexp.PermissionResourceDump;
import eco.ywhc.xr.common.model.dto.req.PermissionResourceReq;
import eco.ywhc.xr.common.model.dto.res.PermissionResourceRes;
import eco.ywhc.xr.common.model.entity.Permission;
import eco.ywhc.xr.common.model.entity.PermissionResource;
import eco.ywhc.xr.core.manager.PermissionManager;
import eco.ywhc.xr.core.manager.PermissionResourceManager;
import eco.ywhc.xr.core.mapper.PermissionResourceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.ConditionNotMetException;
import org.sugar.commons.exception.InvalidInputException;
import org.sugar.commons.exception.UniqueViolationException;
import org.sugar.crud.model.PageableModelSet;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionResourceServiceImpl implements PermissionResourceService {

    private final PermissionResourceConverter permissionResourceConverter;

    private final PermissionResourceMapper permissionResourceMapper;

    private final PermissionResourceManager permissionResourceManager;

    private final PermissionManager permissionManager;

    @Override
    public Long createOne(PermissionResourceReq req) {
        validateRequest(req, null);
        PermissionResource permissionResource = permissionResourceConverter.fromRequest(req);
        permissionResourceMapper.insert(permissionResource);
        return permissionResource.getId();
    }

    @Override
    public PageableModelSet<PermissionResourceRes> findMany() {
        QueryWrapper<PermissionResource> qw = new QueryWrapper<>();
        qw.lambda().eq(PermissionResource::getDeleted, 0)
                .orderByDesc(PermissionResource::getBuiltIn)
                .orderByAsc(PermissionResource::getCode);
        List<PermissionResourceRes> rows = permissionResourceMapper.selectList(qw).stream()
                .map(permissionResourceConverter::toResponse)
                .toList();
        return PageableModelSet.of(rows);
    }

    @Override
    public PermissionResourceRes findOne(Long id) {
        PermissionResource permissionResource = permissionResourceManager.mustFoundEntityById(id);
        return permissionResourceConverter.toResponse(permissionResource);
    }

    @Override
    public int updateOne(Long id, PermissionResourceReq req) {
        validateRequest(req, id);
        PermissionResource permissionResource = permissionResourceManager.mustFoundEntityById(id);
        if (permissionResource.getBuiltIn()) {
            throw new InvalidInputException("不能修改内置权限资源");
        }
        if (!permissionResource.getCode().equals(req.getCode())) {
            throw new InvalidInputException("不能修改权限资源的编码");
        }
        permissionResourceConverter.update(req, permissionResource);
        return permissionResourceMapper.updateById(permissionResource);
    }

    @Override
    public int deleteOne(Long id, boolean logical) {
        PermissionResource permissionResource = permissionResourceManager.mustFoundEntityById(id);
        if (permissionResource.getBuiltIn()) {
            throw new InvalidInputException("不能删除内置权限资源");
        }
        List<Permission> permissions = permissionManager.findAllEntitiesByResourceCode(permissionResource.getCode());
        if (CollectionUtils.isNotEmpty(permissions)) {
            throw new ConditionNotMetException("权限资源仍在使用，不能删除");
        }
        return permissionResourceMapper.deleteEntityById(id, logical);
    }

    @Override
    public List<PermissionResourceDump> exportAll() {
        QueryWrapper<PermissionResource> qw = new QueryWrapper<>();
        qw.lambda().eq(PermissionResource::getDeleted, false)
                .eq(PermissionResource::getBuiltIn, false)
                .orderByAsc(PermissionResource::getId);
        final List<PermissionResource> permissionResources = permissionResourceMapper.selectList(qw);
        return permissionResourceConverter.toDump(permissionResources);
    }

    @Override
    public int importAll(Collection<PermissionResourceDump> dumpList) {
        if (CollectionUtils.isEmpty(dumpList)) {
            return 0;
        }
        final List<PermissionResource> permissionResources = permissionResourceConverter.fromDump(List.copyOf(dumpList));
        final Set<String> codes = permissionResources.stream()
                .map(PermissionResource::getCode)
                .collect(Collectors.toSet());
        if (permissionResourceManager.anyExists(codes)) {
            throw new UniqueViolationException("权限资源数据和现有数据冲突，导入失败");
        }
        return permissionResourceMapper.bulkInsert(permissionResources);
    }

    private void validateRequest(PermissionResourceReq req, @Nullable Long id) {
        PermissionResource found = permissionResourceManager.findEntityByCode(req.getCode());
        if (found != null && !found.getId().equals(id)) {
            throw new UniqueViolationException("权限资源编码已被使用");
        }
    }

}
