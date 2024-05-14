package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.req.MeasurementDataTimeReq;
import eco.ywhc.xr.common.model.dto.req.MonthlyElectricityPriceReq;
import eco.ywhc.xr.common.model.dto.req.RegionalMeasurementDataReq;
import eco.ywhc.xr.common.model.dto.res.MeasurementDataTimeRes;
import eco.ywhc.xr.common.model.dto.res.MonthlyElectricityPriceRes;
import eco.ywhc.xr.common.model.dto.res.RegionalMeasurementDataRes;
import eco.ywhc.xr.common.model.entity.RegionalMeasurementData;
import eco.ywhc.xr.common.model.query.RegionalMeasurementDataQuery;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.manager.BaseManager;

import java.util.List;

@Transactional(rollbackFor = {Exception.class})
public interface RegionalMeasurementDataManager extends BaseManager<Long, RegionalMeasurementData, RegionalMeasurementDataReq, RegionalMeasurementDataRes, RegionalMeasurementDataQuery> {

    /**
     * 插入区域测算数据
     *
     * @param req 区域测算数据请求
     */
    Long insertRegionalMeasurementData(RegionalMeasurementDataReq req);

    /**
     * 批量插入测算数据时间
     *
     * @param id   区域测算数据ID
     * @param reqs 测算数据时间请求
     */
    void bulkInsertMeasurementDataTime(long id, List<MeasurementDataTimeReq> reqs);

    /**
     * 批量插入月度电价
     *
     * @param id   区域测算数据ID
     * @param reqs 月度电价请求
     */
    void bulkInsertMonthlyElectricityPrice(long id, List<MonthlyElectricityPriceReq> reqs);

    /**
     * 根据ID查找单个区域测算数据
     *
     * @param id 区域测算数据ID
     */
    RegionalMeasurementDataRes findOne(long id);

    /**
     * 根据区域测算数据ID查找测算数据时间
     *
     * @param refId 区域测算数据ID
     */
    List<MeasurementDataTimeRes> findMeasurementDataTimesByRefId(long refId);

    /**
     * 根据区域测算数据ID查找月度电价
     *
     * @param refId 区域测算数据ID
     */
    List<MonthlyElectricityPriceRes> findMonthlyElectricityPricesByRefId(long refId);

    /**
     * 逻辑删除测算数据时间和月度电价
     *
     * @param refId 区域测算数据ID
     */
    void logicDeleteMeasurementDataTimeAndMonthlyElectricityPriceByRefId(long refId);

    /**
     * 检查时间是否合法
     *
     * @param req 测算数据时间请求
     */
    boolean isValid(MeasurementDataTimeReq req);

    /**
     * 根据ID查找区域测算数据
     *
     * @param id 区域测算数据ID
     */
    @Override
    RegionalMeasurementData findEntityById(@NonNull Long id);

}
