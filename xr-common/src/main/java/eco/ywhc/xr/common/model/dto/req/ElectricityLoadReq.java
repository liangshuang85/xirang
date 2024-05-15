package eco.ywhc.xr.common.model.dto.req;

import lombok.Data;
import org.sugar.crud.model.BaseRestRequest;

import java.math.BigDecimal;

/**
 * 用电企业负荷相关信息Req
 */
@Data
public class ElectricityLoadReq implements BaseRestRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 用电企业名称
     */
    private String name;

    /**
     * 年用电量(kWh)
     */
    private BigDecimal annualElectricityUsage;

    /**
     * 电力负荷(kW) - 注意：单位应为kW，而不是kwh，除非有特殊含义
     */
    private BigDecimal electricityLoad;

    /**
     * 企业主要生产用电时间段
     */
    private String mainProductionTime;

    /**
     * 主要生产时间段用电量(kWh)
     */
    private BigDecimal mainProductionElectricityUsage;

    /**
     * 园区综合电价(￥/kWh)
     */
    private BigDecimal parkComprehensiveElectricityPrice;

}
