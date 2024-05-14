package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.req.RegionalMeasurementDataReq;
import eco.ywhc.xr.common.model.dto.res.RegionalMeasurementDataRes;
import eco.ywhc.xr.common.model.entity.RegionalMeasurementData;
import eco.ywhc.xr.common.model.query.RegionalMeasurementDataQuery;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.model.PageableModelSet;
import org.sugar.crud.service.BaseService;

@Transactional(rollbackFor = {Exception.class})
public interface RegionalMeasurementDataService extends
        BaseService<Long, RegionalMeasurementData, RegionalMeasurementDataReq, RegionalMeasurementDataRes, RegionalMeasurementDataQuery> {

    /**
     * 创建一个新的区域测算数据记录
     *
     * @param req 区域测算数据请求，不能为空
     */
    @Override
    Long createOne(@NonNull RegionalMeasurementDataReq req);

    /**
     * 根据给定的ID查找单个区域测算数据记录
     *
     * @param id 区域测算数据记录的唯一标识ID，不能为空
     */
    @Override
    RegionalMeasurementDataRes findOne(@NonNull Long id);

    @Override
    PageableModelSet<RegionalMeasurementDataRes> findMany(@NonNull RegionalMeasurementDataQuery query);

    @Override
    int updateOne(@NonNull Long id, @NonNull RegionalMeasurementDataReq req);

    @Override
    int logicDeleteOne(@NonNull Long id);

}
