package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.ksuid.Ksuid;
import eco.ywhc.xr.common.constant.KeypairGeneratorAlgorithm;
import eco.ywhc.xr.common.converter.ApplicationConverter;
import eco.ywhc.xr.common.model.PemKeyPair;
import eco.ywhc.xr.common.model.dto.req.ApplicationReq;
import eco.ywhc.xr.common.model.dto.res.ApplicationRes;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.entity.Application;
import eco.ywhc.xr.common.model.query.ApplicationQuery;
import eco.ywhc.xr.core.manager.ApplicationManager;
import eco.ywhc.xr.core.manager.JwtManager;
import eco.ywhc.xr.core.manager.PermissionManager;
import eco.ywhc.xr.core.mapper.ApplicationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InvalidInputException;
import org.sugar.crud.model.PageableModelSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationConverter applicationConverter;

    private final ApplicationManager applicationManager;

    private final ApplicationMapper applicationMapper;

    private final JwtManager jwtManager;

    private final PermissionManager permissionManager;

    @Override
    public Long createOne(ApplicationReq req) {
        validateRequest(req, null);

        final Application application = applicationConverter.fromRequest(req);
        final String applicationSid = "app_" + Ksuid.newKsuid().toString();
        application.setApplicationSid(applicationSid);
        applicationMapper.insert(application);

        final long id = application.getId();
        applicationManager.grantPermissions(id, req.getScopes());
        return id;
    }

    @Override
    public PageableModelSet<ApplicationRes> findMany(ApplicationQuery query) {
        QueryWrapper<Application> qw = new QueryWrapper<>();
        qw.lambda().eq(Application::getDeleted, 0);
        var rst = applicationMapper.selectPage(query.paging(true), qw)
                .convert(applicationConverter::toResponse);
        if (query.isNoPaging()) {
            return PageableModelSet.of(rst.getRecords());
        }
        return PageableModelSet.from(rst);
    }

    @Override
    public ApplicationRes findOne(long id) {
        Application entity = applicationManager.mustFoundEntityById(id);
        ApplicationRes response = applicationConverter.toResponse(entity);
        List<PermissionRes> permissions = listPermissions(id);
        response.setScopes(permissions);
        return response;
    }

    @Override
    public int updateOne(long id, ApplicationReq req) {
        Application entity = applicationManager.mustFoundEntityById(id);
        applicationConverter.update(req, entity);
        int affected = applicationMapper.updateById(entity);
        configurePermissionsInternal(entity.getId(), req.getScopes());
        return affected;
    }

    @Override
    public int deleteOne(long id, boolean logical) {
        applicationManager.mustFoundEntityById(id);
        return applicationMapper.deleteEntityById(id, logical);
    }

    @Override
    public void enable(long id) {
        Application application = applicationManager.mustFoundEntityById(id);
        if (Boolean.TRUE.equals(application.getEnabled())) {
            return;
        }
        UpdateWrapper<Application> uw = new UpdateWrapper<>();
        uw.lambda().eq(Application::getId, id).set(Application::getEnabled, true);
        applicationMapper.update(uw);
    }

    @Override
    public void disable(long id) {
        Application application = applicationManager.mustFoundEntityById(id);
        if (Boolean.FALSE.equals(application.getEnabled())) {
            return;
        }
        UpdateWrapper<Application> uw = new UpdateWrapper<>();
        uw.lambda().eq(Application::getId, id).set(Application::getEnabled, false);
        applicationMapper.update(uw);
    }

    @Override
    public void configurePermissions(long id, Collection<String> permissionCodes) {
        applicationManager.mustFoundEntityById(id);
        configurePermissionsInternal(id, permissionCodes);
    }

    @Override
    public List<PermissionRes> listPermissions(long id) {
        Set<String> currentPermissionCodes = applicationManager.findCurrentPermissionCodes(id);
        return permissionManager.findAllByPermissionCodes(currentPermissionCodes);
    }

    @Override
    public PemKeyPair generatePemKeyPair(long id, KeypairGeneratorAlgorithm algorithm) {
        Application application = applicationManager.mustFoundEntityById(id);
        final PemKeyPair keyPair = jwtManager.generatePemKeyPair(algorithm.name());
        application.setPublicKey(keyPair.getPublicKey());
        application.setAlgorithm(algorithm);
        applicationMapper.updateById(application);
        return keyPair;
    }

    private void validateRequest(ApplicationReq req, @Nullable Long id) {
        if (CollectionUtils.isNotEmpty(req.getScopes())) {
            if (!permissionManager.allExist(req.getScopes())) {
                throw new InvalidInputException("应用授权Scope列表错误");
            }
        }
    }

    private void validatePermissionCodes(Collection<String> permissionCodes) {
        // 检查权限编码集合是否合法
        if (CollectionUtils.isEmpty(permissionCodes)) {
            return;
        }
        if (!permissionManager.allExist(permissionCodes)) {
            throw new InvalidInputException("权限编码列表错误");
        }
    }

    private void configurePermissionsInternal(long id, Collection<String> permissionCodes) {
        validatePermissionCodes(permissionCodes);
        Set<String> currentPermissionCodes = applicationManager.findCurrentPermissionCodes(id);
        // 撤销
        Set<String> pendingToRevoke = new HashSet<>(currentPermissionCodes);
        pendingToRevoke.removeAll(permissionCodes);
        if (CollectionUtils.isNotEmpty(pendingToRevoke)) {
            applicationManager.revokePermissions(id, pendingToRevoke);
        }
        // 授予
        if (CollectionUtils.isNotEmpty(permissionCodes)) {
            applicationManager.grantPermissions(id, permissionCodes);
        }
    }

}
