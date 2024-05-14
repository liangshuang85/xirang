package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.common.model.dto.req.RegionalMeasurementDataReq;
import eco.ywhc.xr.common.model.dto.res.RegionalMeasurementDataRes;
import eco.ywhc.xr.common.model.query.RegionalMeasurementDataQuery;
import eco.ywhc.xr.core.service.RegionalMeasurementDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.sugar.crud.model.CreateResult;
import org.sugar.crud.model.OperationResult;
import org.sugar.crud.model.PageableModelSet;

/**
 * 区域测算数据接口
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
public class RegionalMeasurementDataController {

    private final RegionalMeasurementDataService regionalMeasurementDataService;

    /**
     * 新建一个区域测算数据
     */
    @PostMapping("/regionalMeasurementData")
    public CreateResult<Long> createOne(@Valid @RequestBody RegionalMeasurementDataReq req) {
        Long id = regionalMeasurementDataService.createOne(req);
        return CreateResult.of(id);
    }

    /**
     * 获取区域测算数据列表，结果以分页形式返回
     *
     * @param query 区域测算数据查询条件
     */
    @GetMapping("/regionalMeasurementData")
    public PageableModelSet<RegionalMeasurementDataRes> findMany(@Valid @ParameterObject RegionalMeasurementDataQuery query) {
        return regionalMeasurementDataService.findMany(query);
    }

    /**
     * 获取指定区域测算数据
     *
     * @param id 区域测算数据ID
     */
    @GetMapping("/regionalMeasurementData/{id}")
    public RegionalMeasurementDataRes findOne(@PathVariable Long id) {
        return regionalMeasurementDataService.findOne(id);
    }

    /**
     * 更新指定区域测算数据
     *
     * @param id  区域测算数据ID
     * @param req 区域测算数据请求
     */
    @PutMapping("/regionalMeasurementData/{id}")
    public OperationResult updateOne(@PathVariable Long id, @Valid @RequestBody RegionalMeasurementDataReq req) {
        int affected = regionalMeasurementDataService.updateOne(id, req);
        return OperationResult.of(affected);
    }

    /**
     * 删除指定区域测算数据
     *
     * @param id 区域测算数据ID
     */
    @DeleteMapping("/regionalMeasurementData/{id}")
    public OperationResult deleteOne(@PathVariable Long id) {
        int affected = regionalMeasurementDataService.logicDeleteOne(id);
        return OperationResult.of(affected);
    }

}
