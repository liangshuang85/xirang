package eco.ywhc.xr.common.model.dto.res;

import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * 区域测算数据表返回实体类
 */
@Data
public class RegionalMeasurementDataRes implements BaseRestResponse {

    /**
     * id
     */
    private Long id;

    /**
     * 6位数字格式的行政区划代码
     */
    private Long adcode;

    /**
     * 市场充电价格(￥ /kWh)
     */
    private BigDecimal marketChargingPrice;

    /**
     * 固定充电价格(￥ /kWh)
     */
    private BigDecimal staticChargingPrice;

    /**
     * 充电价格(￥/kwh)
     */
    private BigDecimal chargingPrice;

    /**
     * 充电补贴(￥/kWh)
     */
    private BigDecimal chargingSubsidy;

    /**
     * 充电补贴年限(年)
     */
    private Integer chargingSubsidyYears;

    /**
     * 放电补贴(￥/kWh)
     */
    private BigDecimal dischargingSubsidy;

    /**
     * 放电补贴年限(年)
     */
    private Integer dischargingSubsidyYears;

    /**
     * 放电价格(￥/kwh)
     */
    private BigDecimal dischargingPrice;

    /**
     * 租赁价格(￥/kwh·年)
     */
    private BigDecimal leasingPrice;

    /**
     * 碳排放因子(tCO:/MWh)
     */
    private BigDecimal carbonEmissionFactor;

    /**
     * 增值税%
     */
    private BigDecimal vat;

    /**
     * 城建税%
     */
    private BigDecimal urbanConstructionTax;

    /**
     * 教育费%
     */
    private BigDecimal educationFee;

    /**
     * 所得税%
     */
    private BigDecimal incomeTax;

    /**
     * 碳资产增值税%
     */
    private BigDecimal carbonAssetValueAddedTax;

    /**
     * 所得税理由
     */
    private String incomeTaxJustification;

    /**
     * 尖峰电价(￥ /kwh)
     */
    private BigDecimal topPrice;

    /**
     * 平段电价(￥/kwh)
     */
    private BigDecimal normalPrice;

    /**
     * 低谷电价(￥/kWh)
     */
    private BigDecimal valleyPrice;

    /**
     * 线损电价(￥/kWh)
     */
    private BigDecimal lineLossPrice;

    /**
     * 系统运行费折价(￥ /kWh)
     */
    private BigDecimal operationFeeDiscount;

    /**
     * 上网环节线损电价(￥ /kWh)
     */
    private BigDecimal gridConnectionLineLossPrice;

    /**
     * 高峰电价(￥ /kwh)
     */
    private BigDecimal peakPrice;

    /**
     * 深谷电价(￥/kwh)
     */
    private BigDecimal deepPrice;

    /**
     * 测算数据时段表
     */
    private List<MeasurementDataTimeRes> measurementDataTimes;

    /**
     * 月电价表
     */
    private List<MonthlyElectricityPriceRes> monthlyElectricityPrices;

    /**
     * 行政区
     */
    private AdministrativeDivisionRes administrativeDivision;

}
