package eco.ywhc.xr.common.model.dto.res;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class BasicDataRes {

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 已规划风力发电资源（GW）
     */
    private String windPowerResource;

    /**
     * 已规划光伏发电资源(GW)
     */
    private String photovoltaicResource;

    /**
     * 新能源并网电价(￥/kWh)
     */
    private Double gridEnergyPrice;

    /**
     * 新能源配套储能标准装机量比例(%)
     */
    private Double storageStandardsProportion;

    /**
     * 风力及光伏发电弃电量(kWh)
     */
    private Double energyDiscarded;

    /**
     * 内蒙专有：新能源可并网电量比例(%)
     */
    private Double energyGridElectricityProportion;

    /**
     * 风力等效发电时长(h/年)
     */
    private Double windOperationHours;

    /**
     * 光伏等效发电时长（h/年）
     */
    private Double pvOperationHours;

    /**
     * 新能源配套储能标准时长（h）
     */
    private Double energyStorageStandardHours;

    /**
     * 制氢可下电量比例（%）
     */
    private Double htdrogenElectricityProportion;

    /**
     * 供暖温度(℃)
     */
    private Double heatTemperature;

    /**
     * 民用供暖价格（￥/㎡/月）
     */
    private Double civilHeatPrices;

    /**
     * 工商业供暖价格（￥/㎡/月）
     */
    private Double industryHeatPrices;

    /**
     * 年供暖时长（天）
     */
    private Double heatDurationHours;

    /**
     * 民用供暖面积(㎡)
     */
    private Double civilHeatArea;

    /**
     * 工商业用供暖面积(㎡)
     */
    private Double industryHeatArea;

    /**
     * 绿氢卖出（￥/t）
     */
    private Double greenOxygenPrice;

    /**
     * 储能电站与电网接入变电站距离（公里）
     */
    private Double energyStorageStationGridDistance;

    /**
     * 接入电网缆线成本（￥/公里）
     */
    private Double powerGridPrice;

    /**
     * 新能源电站与储能电站距离（公里）
     */
    private Double energyPowerEnergySorageDistance;

    /**
     * 储能电站用地价格（￥/亩）
     */
    private Double energyStorgeLandPrice;

    /**
     * 新能源用地租金成本（￥/亩）
     */
    private Double rentEnergyLandPrice;

    /**
     * 社平工资（￥）
     */
    private Double socialAverageSalary;

    /**
     * 社保相关费用（￥）
     */
    private Double socialSecurityExpenses;

    /**
     * 长期贷款利率（%）
     */
    private Double longLoanRate;

    /**
     * 长期贷款宽限期(年)
     */
    private Long longLoanGracePeriod;

    /**
     * 长期贷款年限(年)
     */
    private Long longLoanYear;

    /**
     * 短期贷款利率（%）
     */
    private Double shortLoanRate;

    /**
     * 短期贷款年限(年)
     */
    private Long shortLoanYear;

    /**
     * 液氧卖出（￥/t)
     */
    private BigDecimal oxygenPrice;

    /**
     * 碳交易价（￥/tCO₂)
     */
    private BigDecimal carbonTradePrice;

    /**
     * 用水（￥/t)
     */
    private BigDecimal waterPrice;

    /**
     * 用电企业负荷相关信息
     */
    private List<ElectricityLoadRes> electricityLoads = new ArrayList<>();

    /**
     * 用户企业液氧/绿氢使用量
     */
    private List<OxygenHydrogenUsageRes> oxygenHydrogenUsages = new ArrayList<>();

}
