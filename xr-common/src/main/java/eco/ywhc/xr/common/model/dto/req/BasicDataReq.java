package eco.ywhc.xr.common.model.dto.req;

import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Value
public class BasicDataReq implements BaseRestRequest {

    /**
     * 经度
     */
    String longitude;

    /**
     * 纬度
     */
    String latitude;

    /**
     * 已规划风力发电资源（GW）
     */
    String windPowerResource;

    /**
     * 已规划光伏发电资源(GW)
     */
    String photovoltaicResource;

    /**
     * 新能源并网电价(￥/kWh)
     */
    Double gridEnergyPrice;

    /**
     * 新能源配套储能标准装机量比例(%)
     */
    Double storageStandardsProportion;

    /**
     * 风力及光伏发电弃电量(kWh)
     */
    Double energyDiscarded;

    /**
     * 内蒙专有：新能源可并网电量比例(%)
     */
    Double energyGridElectricityProportion;

    /**
     * 风力等效发电时长(h/年)
     */
    Double windOperationHours;

    /**
     * 光伏等效发电时长（h/年）
     */
    Double pvOperationHours;

    /**
     * 新能源配套储能标准时长（h）
     */
    Double energyStorageStandardHours;

    /**
     * 制氢可下电量比例（%）
     */
    Double htdrogenElectricityProportion;

    /**
     * 供暖温度(℃)
     */
    Double heatTemperature;

    /**
     * 民用供暖价格（￥/㎡/月）
     */
    Double civilHeatPrices;

    /**
     * 工商业供暖价格（￥/㎡/月）
     */
    Double industryHeatPrices;

    /**
     * 年供暖时长（天）
     */
    Double heatDurationHours;

    /**
     * 民用供暖面积(㎡)
     */
    Double civilHeatArea;

    /**
     * 工商业用供暖面积(㎡)
     */
    Double industryHeatArea;

    /**
     * 绿氢卖出（￥/t）
     */
    Double greenOxygenPrice;

    /**
     * 储能电站与电网接入变电站距离（公里）
     */
    Double energyStorageStationGridDistance;

    /**
     * 接入电网缆线成本（￥/公里）
     */
    Double powerGridPrice;

    /**
     * 新能源电站与储能电站距离（公里）
     */
    Double energyPowerEnergySorageDistance;

    /**
     * 储能电站用地价格（￥/亩）
     */
    Double energyStorgeLandPrice;

    /**
     * 新能源用地租金成本（￥/亩）
     */
    Double rentEnergyLandPrice;

    /**
     * 社平工资（￥）
     */
    Double socialAverageSalary;

    /**
     * 社保相关费用（￥）
     */
    Double socialSecurityExpenses;

    /**
     * 长期贷款利率（%）
     */
    Double longLoanRate;

    /**
     * 长期贷款宽限期(年)
     */
    Long longLoanGracePeriod;

    /**
     * 长期贷款年限(年)
     */
    Long longLoanYear;

    /**
     * 短期贷款利率（%）
     */
    Double shortLoanRate;

    /**
     * 短期贷款年限(年)
     */
    Long shortLoanYear;

    /**
     * 液氧卖出（￥/t)
     */
    BigDecimal oxygenPrice;

    /**
     * 碳交易价（￥/tCO₂)
     */
    BigDecimal carbonTradePrice;

    /**
     * 用水（￥/t)
     */
    BigDecimal waterPrice;

    /**
     * 用电企业负荷相关信息
     */
    List<ElectricityLoadReq> electricityLoads = new ArrayList<>();

    /**
     * 用户企业液氧/绿氢使用量
     */
    List<OxygenHydrogenUsageReq> oxygenHydrogenUsages = new ArrayList<>();

}
