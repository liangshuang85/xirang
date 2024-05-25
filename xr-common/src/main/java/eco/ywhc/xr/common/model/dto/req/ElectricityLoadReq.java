package eco.ywhc.xr.common.model.dto.req;

import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.math.BigDecimal;

/**
 * 用电企业负荷相关信息Req
 */
@Value
public class ElectricityLoadReq implements BaseRestRequest {

    /**
     * ID
     */
    Long id;

    /**
     * 用电企业名称
     */
    String name;

    /**
     * 年用电量(kWh)
     */
    BigDecimal annualElectricityUsage;

    /**
     * 电力负荷(kW) - 注意：单位应为kW，而不是kwh，除非有特殊含义
     */
    BigDecimal electricityLoad;

    /**
     * 企业主要生产用电时间段
     */
    String mainProductionTime;

    /**
     * 主要生产时间段用电量(kWh)
     */
    BigDecimal mainProductionElectricityUsage;

    /**
     * 园区综合电价(￥/kWh)
     */
    BigDecimal parkComprehensiveElectricityPrice;

}
