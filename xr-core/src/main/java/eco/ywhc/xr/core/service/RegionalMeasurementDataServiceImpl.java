package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import eco.ywhc.xr.common.converter.RegionalMeasurementDataConverter;
import eco.ywhc.xr.common.model.dto.req.RegionalMeasurementDataReq;
import eco.ywhc.xr.common.model.dto.res.AdministrativeDivisionRes;
import eco.ywhc.xr.common.model.dto.res.RegionalMeasurementDataRes;
import eco.ywhc.xr.common.model.entity.BaseEntity;
import eco.ywhc.xr.common.model.entity.RegionalMeasurementData;
import eco.ywhc.xr.common.model.query.RegionalMeasurementDataQuery;
import eco.ywhc.xr.core.manager.AdministrativeDivisionManager;
import eco.ywhc.xr.core.manager.RegionalMeasurementDataManager;
import eco.ywhc.xr.core.mapper.RegionalMeasurementDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.crud.model.PageableModelSet;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegionalMeasurementDataServiceImpl implements RegionalMeasurementDataService {

    private final RegionalMeasurementDataMapper regionalMeasurementDataMapper;

    private final RegionalMeasurementDataConverter regionalMeasurementDataConverter;

    private final RegionalMeasurementDataManager regionalMeasurementDataManager;

    private final AdministrativeDivisionManager administrativeDivisionManager;

    @Override
    public Long createOne(@NonNull RegionalMeasurementDataReq req) {
        Long id = regionalMeasurementDataManager.insertRegionalMeasurementData(req);

        regionalMeasurementDataManager.bulkInsertMeasurementDataTime(id, req.getMeasurementDataTimes());
        regionalMeasurementDataManager.bulkInsertMonthlyElectricityPrice(id, req.getMonthlyElectricityPrices());
        return id;
    }

    @Override
    public RegionalMeasurementDataRes findOne(@NonNull Long id) {
        RegionalMeasurementDataRes res = regionalMeasurementDataManager.findOne(id);
        AdministrativeDivisionRes administrativeDivision = administrativeDivisionManager.findByAdcodeSurely(res.getAdcode());
        res.setAdministrativeDivision(administrativeDivision);
        return res;
    }

    @Override
    public PageableModelSet<RegionalMeasurementDataRes> findMany(@NonNull RegionalMeasurementDataQuery query) {
        QueryWrapper<RegionalMeasurementData> qw = new QueryWrapper<>();
        qw.lambda().eq(BaseEntity::getDeleted, false)
                .eq(query.getAdcode() != null, RegionalMeasurementData::getAdcode, query.getAdcode());
        IPage<RegionalMeasurementData> rows = regionalMeasurementDataMapper.selectPage(query.paging(true), qw);

        Set<Long> adcodes = rows.getRecords().stream().map(RegionalMeasurementData::getAdcode).collect(Collectors.toSet());
        Map<Long, AdministrativeDivisionRes> administrativeDivisionMap = administrativeDivisionManager.findAllAsMapByAdcodesSurely(adcodes);

        IPage<RegionalMeasurementDataRes> results = rows.convert(i -> {
            RegionalMeasurementDataRes res = regionalMeasurementDataConverter.toResponse(i);
            res.setAdministrativeDivision(administrativeDivisionMap.get(i.getAdcode()));
            res.setMeasurementDataTimes(regionalMeasurementDataManager.findMeasurementDataTimesByRefId(i.getId()));
            res.setMonthlyElectricityPrices(regionalMeasurementDataManager.findMonthlyElectricityPricesByRefId(i.getId()));
            return res;
        });
        return PageableModelSet.from(results);
    }

    @Override
    public int updateOne(@NonNull Long id, @NonNull RegionalMeasurementDataReq req) {
        RegionalMeasurementData regionalMeasurementData = regionalMeasurementDataManager.mustFoundEntityById(id);
        regionalMeasurementDataConverter.update(req, regionalMeasurementData);
        int affact = regionalMeasurementDataMapper.updateById(regionalMeasurementData);

        regionalMeasurementDataManager.logicDeleteMeasurementDataTimeAndMonthlyElectricityPriceByRefId(id);
        regionalMeasurementDataManager.bulkInsertMeasurementDataTime(id, req.getMeasurementDataTimes());
        regionalMeasurementDataManager.bulkInsertMonthlyElectricityPrice(id, req.getMonthlyElectricityPrices());
        return affact;
    }

    @Override
    public int logicDeleteOne(@NonNull Long id) {
        regionalMeasurementDataManager.logicDeleteMeasurementDataTimeAndMonthlyElectricityPriceByRefId(id);
        return regionalMeasurementDataMapper.logicDeleteEntityById(id);
    }

}
