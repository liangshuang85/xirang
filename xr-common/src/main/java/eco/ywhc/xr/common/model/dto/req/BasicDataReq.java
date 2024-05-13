package eco.ywhc.xr.common.model.dto.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class BasicDataReq {

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 光伏装机量（MW)
     */
    private BigDecimal installedCapacity;

    /**
     * 液氧卖出（￥/t)
     */
    private BigDecimal oxygenPrice;

    /**
     * 供暖价格（￥/GJ)
     */
    private BigDecimal heatingPrice;

    /**
     * 供暖总量（GJ)
     */
    private BigDecimal totalHeating;

    /**
     * 碳交易价（￥/tCO₂)
     */
    private BigDecimal carbonTradePrice;

    /**
     * 用水（￥/t)
     */
    private BigDecimal waterPrice;

    /**
     * 贷款利率（%)
     */
    private BigDecimal loanRate;

    /**
     * 贷款年限（年)
     */
    private BigDecimal loanYears;

}
