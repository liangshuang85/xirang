package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 区域测算数据表请求实体类
 */
@Value
public class RegionalMeasurementDataReq implements BaseRestRequest {

    /**
     * 6位数字格式的行政区划代码
     */
    Long adcode;

    /**
     * 市场充电价格(￥ /kWh)
     */
    BigDecimal marketChargingPrice;

    /**
     * 固定充电价格(￥ /kWh)
     */
    BigDecimal staticChargingPrice;

    /**
     * 充电价格(￥/kwh)
     */
    BigDecimal chargingPrice;

    /**
     * 充电补贴(￥/kWh)
     */
    BigDecimal chargingSubsidy;

    /**
     * 充电补贴年限(年)
     */
    Integer chargingSubsidyYears;

    /**
     * 放电补贴(￥/kWh)
     */
    BigDecimal dischargingSubsidy;

    /**
     * 放电补贴年限(年)
     */
    Integer dischargingSubsidyYears;

    /**
     * 放电价格(￥/kwh)
     */
    BigDecimal dischargingPrice;

    /**
     * 租赁价格(￥/kwh·年)
     */
    BigDecimal leasingPrice;

    /**
     * 碳排放因子(tCO:/MWh)
     */
    BigDecimal carbonEmissionFactor;

    /**
     * 增值税%
     */
    BigDecimal vat;

    /**
     * 城建税%
     */
    BigDecimal urbanConstructionTax;

    /**
     * 教育费%
     */
    BigDecimal educationFee;

    /**
     * 所得税%
     */
    BigDecimal incomeTax;

    /**
     * 碳资产增值税%
     */
    BigDecimal carbonAssetValueAddedTax;

    /**
     * 所得税理由
     */
    String incomeTaxJustification;

    /**
     * 尖峰电价(￥ /kwh)
     */
    BigDecimal topPrice;

    /**
     * 平段电价(￥/kwh)
     */
    BigDecimal normalPrice;

    /**
     * 低谷电价(￥/kWh)
     */
    BigDecimal valleyPrice;

    /**
     * 线损电价(￥/kWh)
     */
    BigDecimal lineLossPrice;

    /**
     * 系统运行费折价(￥ /kWh)
     */
    BigDecimal operationFeeDiscount;

    /**
     * 上网环节线损电价(￥ /kWh)
     */
    BigDecimal gridConnectionLineLossPrice;

    /**
     * 高峰电价(￥ /kwh)
     */
    BigDecimal peakPrice;

    /**
     * 深谷电价(￥/kwh)
     */
    BigDecimal deepPrice;

    /**
     * 测算数据时段表
     */
    @NotNull
    @Valid
    List<MeasurementDataTimeReq> measurementDataTimes = new ArrayList<>();

    /**
     * 月电价表
     */
    @NotNull
    @Valid
    List<MonthlyElectricityPriceReq> monthlyElectricityPrices = new ArrayList<>();

}
