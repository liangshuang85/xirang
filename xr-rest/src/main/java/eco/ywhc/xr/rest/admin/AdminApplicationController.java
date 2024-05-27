package eco.ywhc.xr.rest.admin;

import eco.ywhc.xr.common.model.PemKeyPair;
import eco.ywhc.xr.common.model.dto.req.ApplicationReq;
import eco.ywhc.xr.common.model.dto.req.KeyPairGenerateReq;
import eco.ywhc.xr.common.model.dto.req.ScopeConfigureReq;
import eco.ywhc.xr.common.model.dto.res.ApplicationRes;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.query.ApplicationQuery;
import eco.ywhc.xr.core.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.sugar.crud.model.OperationResult;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;

/**
 * 应用接口
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminApplicationController {

    private final ApplicationService applicationService;

    /**
     * 创建一个应用
     */
    @PostMapping("/admin/rest/applications")
    public Long createOne(@RequestBody @Valid ApplicationReq req) {
        return applicationService.createOne(req);
    }

    /**
     * 获取应用列表，结果以分页形式返回
     */
    @GetMapping("/admin/rest/applications")
    public PageableModelSet<ApplicationRes> findMany(@ParameterObject @Valid ApplicationQuery query) {
        return applicationService.findMany(query);
    }

    /**
     * 获取指定ID应用
     *
     * @param id 应用ID
     */
    @GetMapping("/admin/rest/applications/{id}")
    public ApplicationRes findOne(@PathVariable long id) {
        return applicationService.findOne(id);
    }

    /**
     * 获取指定应用的授权Scope列表
     */
    @GetMapping("/admin/rest/applications/{id}/scopes")
    public PageableModelSet<PermissionRes> listPermissions(@PathVariable long id) {
        List<PermissionRes> permissions = applicationService.listPermissions(id);
        return PageableModelSet.of(permissions);
    }

    /**
     * 更新一个应用
     *
     * @param id 应用ID
     */
    @PutMapping("/admin/rest/applications/{id}")
    public OperationResult updateOne(@PathVariable long id, @RequestBody @Valid ApplicationReq req) {
        int affected = applicationService.updateOne(id, req);
        return OperationResult.of(affected);
    }

    /**
     * 配置指定应用的授权Scope列表
     */
    @PutMapping("/admin/rest/applications/{id}/scopes")
    public OperationResult configurePermissions(@PathVariable long id, @Valid @RequestBody ScopeConfigureReq req) {
        applicationService.configurePermissions(id, req.getScopes());
        return OperationResult.of(1);
    }

    /**
     * 删除一个应用
     *
     * @param id 应用ID
     */
    @DeleteMapping("/admin/rest/applications/{id}")
    public OperationResult deleteOne(@PathVariable long id) {
        int affected = applicationService.deleteOne(id, true);
        return OperationResult.of(affected);
    }

    /**
     * 禁用指定应用
     *
     * @param id 应用ID
     */
    @PostMapping("/admin/rest/applications/{id}:disable")
    public OperationResult disable(@PathVariable long id) {
        applicationService.disable(id);
        return OperationResult.of(1);
    }

    /**
     * 启用指定应用
     */
    @PostMapping("/admin/rest/applications/{id}:enable")
    public OperationResult enable(@PathVariable long id) {
        applicationService.enable(id);
        return OperationResult.of(1);
    }

    /**
     * 生成指定应用的密钥对
     *
     * @param id 应用ID
     */
    @PostMapping("/admin/rest/applications/{id}:generateKeyPair")
    public PemKeyPair generatePemKeyPair(@PathVariable long id,
                                         @Valid @RequestBody KeyPairGenerateReq req) {
        return applicationService.generatePemKeyPair(id, req.getAlgorithm());
    }

}
