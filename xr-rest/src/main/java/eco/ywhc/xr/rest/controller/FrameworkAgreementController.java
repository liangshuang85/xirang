package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.common.constant.FrameworkAgreementType;
import eco.ywhc.xr.common.model.FrameworkAgreementStatus;
import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementReq;
import eco.ywhc.xr.common.model.dto.res.FrameworkAgreementRes;
import eco.ywhc.xr.common.model.query.FrameworkAgreementQuery;
import eco.ywhc.xr.core.service.FrameworkAgreementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.sugar.crud.model.CreateResult;
import org.sugar.crud.model.OperationResult;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;
import java.util.Map;

/**
 * 框架协议项目接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class FrameworkAgreementController {

    private final FrameworkAgreementService frameworkAgreementService;

    /**
     * 新建一个框架协议项目
     */
    @PostMapping("/frameworkAgreements")
    public CreateResult<Long> createOne(@Valid @RequestBody FrameworkAgreementReq req) {
        Long id = frameworkAgreementService.createOne(req);
        return CreateResult.of(id);
    }

    /**
     * 获取框架协议项目列表，结果以分页形式返回
     *
     * @param query 框架协议项目查询条件
     */
    @GetMapping("/frameworkAgreements")
    public PageableModelSet<FrameworkAgreementRes> findMany(@Valid @ParameterObject FrameworkAgreementQuery query) {
        return frameworkAgreementService.findMany(query);
    }

    /**
     * 获取指定框架协议项目
     *
     * @param id 框架协议项目ID
     */
    @GetMapping("/frameworkAgreements/{id}")
    public FrameworkAgreementRes findOne(@PathVariable Long id) {
        return frameworkAgreementService.findOne(id);
    }

    /**
     * 修改指定框架协议项目
     *
     * @param id 框架协议项目ID
     */
    @PutMapping("/frameworkAgreements/{id}")
    public OperationResult updateOne(@PathVariable Long id, @Valid @RequestBody FrameworkAgreementReq req) {
        int affected = frameworkAgreementService.updateOne(id, req);
        return OperationResult.of(affected);
    }

    /**
     * 删除指定框架协议项目
     *
     * @param id 项目协议ID
     */
    @DeleteMapping("/frameworkAgreements/{id}")
    public OperationResult logicDeleteOne(@PathVariable Long id) {
        int affected = frameworkAgreementService.logicDeleteOne(id);
        return OperationResult.of(affected);
    }

    /**
     * 获取框架协议项目状态流转表
     * <br>
     * key：框架协议项目当前状态
     * <br>
     * value：当前状态可以变更为的状态列表
     */
    @GetMapping("/frameworkAgreements/status")
    public Map<FrameworkAgreementType, List<FrameworkAgreementType>> getMap() {
        return FrameworkAgreementStatus.getMap();
    }

}
