package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.converter.MeasurementDataTimeConverter;
import eco.ywhc.xr.common.converter.MonthlyElectricityPriceConverter;
import eco.ywhc.xr.common.converter.RegionalMeasurementDataConverter;
import eco.ywhc.xr.common.model.dto.req.MeasurementDataTimeReq;
import eco.ywhc.xr.common.model.dto.req.MonthlyElectricityPriceReq;
import eco.ywhc.xr.common.model.dto.req.RegionalMeasurementDataReq;
import eco.ywhc.xr.common.model.dto.res.MeasurementDataTimeRes;
import eco.ywhc.xr.common.model.dto.res.MonthlyElectricityPriceRes;
import eco.ywhc.xr.common.model.dto.res.RegionalMeasurementDataRes;
import eco.ywhc.xr.common.model.entity.BaseEntity;
import eco.ywhc.xr.common.model.entity.MeasurementDataTime;
import eco.ywhc.xr.common.model.entity.MonthlyElectricityPrice;
import eco.ywhc.xr.common.model.entity.RegionalMeasurementData;
import eco.ywhc.xr.core.mapper.MeasurementDataTimeMapper;
import eco.ywhc.xr.core.mapper.MonthlyElectricityPriceMapper;
import eco.ywhc.xr.core.mapper.RegionalMeasurementDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegionalMeasurementDataManagerImpl implements RegionalMeasurementDataManager {

    private final RegionalMeasurementDataMapper regionalMeasurementDataMapper;

    private final MeasurementDataTimeMapper measurementDataTimeMapper;

    private final MonthlyElectricityPriceMapper monthlyElectricityPriceMapper;

    private final RegionalMeasurementDataConverter regionalMeasurementDataConverter;

    private final MeasurementDataTimeConverter measurementDataTimeConverter;

    private final MonthlyElectricityPriceConverter monthlyElectricityPriceConverter;

    @Override
    public Long insertRegionalMeasurementData(RegionalMeasurementDataReq req) {
        RegionalMeasurementData regionalMeasurementData = regionalMeasurementDataConverter.fromRequest(req);
        regionalMeasurementDataMapper.insert(regionalMeasurementData);
        return regionalMeasurementData.getId();
    }

    @Override
    public void bulkInsertMeasurementDataTime(long id, List<MeasurementDataTimeReq> req) {
        // 检查是否有缺少的月份
        IntStream.rangeClosed(1, 12).forEach(month -> {
            if (req.stream().noneMatch(price -> price.getMonth().equals(month))) {
                throw new IllegalArgumentException("缺少" + month + "月的电价");
            }
        });
        // 检查时间是否合法
        boolean isValid = req.stream().anyMatch(this::isValid);
        if (!isValid) {
            throw new IllegalArgumentException("时间总和不为24小时");
        }
        List<MeasurementDataTime> measurementDataTimes = req.stream()
                .map(measurementDataTimeConverter::fromRequest)
                .peek(measurementDataTime -> measurementDataTime.setRefId(id)).toList();
        measurementDataTimeMapper.bulkInsert(measurementDataTimes);
    }

    @Override
    public void bulkInsertMonthlyElectricityPrice(long id, List<MonthlyElectricityPriceReq> req) {
        // 检查是否有缺少的月份
        IntStream.rangeClosed(1, 12).forEach(month -> {
            if (req.stream().noneMatch(price -> price.getMonth().equals(month))) {
                throw new IllegalArgumentException("缺少" + month + "月的电价");
            }
        });
        List<MonthlyElectricityPrice> monthlyElectricityPrices = req.stream()
                .map(monthlyElectricityPriceConverter::fromRequest)
                .peek(monthlyElectricityPrice -> monthlyElectricityPrice.setRefId(id)).toList();
        monthlyElectricityPriceMapper.bulkInsert(monthlyElectricityPrices);
    }

    @Override
    public RegionalMeasurementDataRes findOne(long id) {
        RegionalMeasurementData regionalMeasurementData = mustFoundEntityById(id);
        RegionalMeasurementDataRes regionalMeasurementDataRes = regionalMeasurementDataConverter.toResponse(regionalMeasurementData);
        regionalMeasurementDataRes.setMeasurementDataTimes(findMeasurementDataTimeByRefId(id));
        regionalMeasurementDataRes.setMonthlyElectricityPrices(findMonthlyElectricityPriceByRefId(id));
        return regionalMeasurementDataRes;
    }

    @Override
    public List<MeasurementDataTimeRes> findMeasurementDataTimesByRefId(long refId) {
        QueryWrapper<MeasurementDataTime> qw = new QueryWrapper<>();
        qw.lambda().eq(BaseEntity::getDeleted, false)
                .eq(MeasurementDataTime::getRefId, refId);
        List<MeasurementDataTime> measurementDataTimes = measurementDataTimeMapper.selectList(qw);
        return measurementDataTimes.stream()
                .map(measurementDataTimeConverter::toResponse)
                .toList();
    }

    @Override
    public List<MonthlyElectricityPriceRes> findMonthlyElectricityPricesByRefId(long refId) {
        QueryWrapper<MonthlyElectricityPrice> qw = new QueryWrapper<>();
        qw.lambda().eq(BaseEntity::getDeleted, false)
                .eq(MonthlyElectricityPrice::getRefId, refId);
        List<MonthlyElectricityPrice> monthlyElectricityPrices = monthlyElectricityPriceMapper.selectList(qw);
        return monthlyElectricityPrices.stream()
                .map(monthlyElectricityPriceConverter::toResponse)
                .toList();
    }

    @Override
    public void logicDeleteMeasurementDataTimeAndMonthlyElectricityPriceByRefId(long refId) {
        QueryWrapper<MeasurementDataTime> qw1 = new QueryWrapper<>();
        qw1.lambda().eq(MeasurementDataTime::getDeleted, false)
                .eq(MeasurementDataTime::getRefId, refId);
        measurementDataTimeMapper.selectList(qw1).stream()
                .peek(measurementDataTime -> measurementDataTime.setDeleted(true))
                .forEach(measurementDataTimeMapper::updateById);

        QueryWrapper<MonthlyElectricityPrice> qw2 = new QueryWrapper<>();
        qw2.lambda().eq(MonthlyElectricityPrice::getDeleted, false)
                .eq(MonthlyElectricityPrice::getRefId, refId);
        monthlyElectricityPriceMapper.selectList(qw2).stream()
                .peek(monthlyElectricityPrice -> monthlyElectricityPrice.setDeleted(true))
                .forEach(monthlyElectricityPriceMapper::updateById);
    }

    private List<MeasurementDataTimeRes> findMeasurementDataTimeByRefId(long id) {
        QueryWrapper<MeasurementDataTime> qw = new QueryWrapper<>();
        qw.lambda().eq(BaseEntity::getDeleted, false)
                .eq(MeasurementDataTime::getRefId, id);
        List<MeasurementDataTime> measurementDataTimes = measurementDataTimeMapper.selectList(qw);
        return measurementDataTimes.stream()
                .map(measurementDataTimeConverter::toResponse)
                .toList();
    }

    private List<MonthlyElectricityPriceRes> findMonthlyElectricityPriceByRefId(long id) {
        QueryWrapper<MonthlyElectricityPrice> qw = new QueryWrapper<>();
        qw.lambda().eq(BaseEntity::getDeleted, false)
                .eq(MonthlyElectricityPrice::getRefId, id);
        List<MonthlyElectricityPrice> monthlyElectricityPrices = monthlyElectricityPriceMapper.selectList(qw);
        return monthlyElectricityPrices.stream()
                .map(monthlyElectricityPriceConverter::toResponse)
                .toList();
    }

    @Override
    public boolean isValid(MeasurementDataTimeReq req) {
        List<List<Integer>> ranges = Stream.of(req.getDeep(), req.getNormal(), req.getPeak(), req.getTop(), req.getValley())
                .flatMap(periods -> Arrays.stream(periods.split(",")))
                .map(timeRange -> {
                    String[] split = timeRange.split("-");
                    return Arrays.asList(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                }).toList();
        boolean isLegal = ranges.stream()
                .allMatch(range -> range.get(0) >= 0 && range.get(1) <= 23);
        if (!isLegal) {
            throw new IllegalArgumentException("时间段不合法");
        }
        boolean noOverLap = ranges.stream()
                .noneMatch(range1 -> ranges.stream()
                        .anyMatch(range2 -> range1 != range2 && range1.get(0) < range2.get(1) && range2.get(0) <= range1.get(1)));
        if (!noOverLap) {
            throw new IllegalArgumentException("时间段重叠");
        }
        List<Integer> allTimes = IntStream
                .rangeClosed(0, 23)
                .boxed()
                .toList();
        return allTimes.stream()
                .allMatch(time -> ranges.stream()
                        .anyMatch(range -> time >= range.get(0) && time <= range.get(1)));
    }

    @Override
    public RegionalMeasurementData findEntityById(@NonNull Long id) {
        return regionalMeasurementDataMapper.selectById(id);
    }

}
