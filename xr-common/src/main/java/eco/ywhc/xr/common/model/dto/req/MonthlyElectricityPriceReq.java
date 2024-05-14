package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.sugar.crud.model.BaseRestRequest;

import java.math.BigDecimal;

/**
 * 月度电价请求实体类
 */
@Data
public class MonthlyElectricityPriceReq implements BaseRestRequest {

    /**
     * 月份
     */
    @NotNull
    private Integer month;

    /**
     * 尖峰电价(￥ /kwh)
     */
    @NotNull
    private BigDecimal topPrice;

    /**
     * 高峰电价(￥ /kwh)
     */
    @NotNull
    private BigDecimal peakPrice;

    /**
     * 平段电价(￥/kwh)
     */
    @NotNull
    private BigDecimal normalPrice;

    /**
     * 低谷电价(￥/kWh)
     */
    @NotNull
    private BigDecimal valleyPrice;

    /**
     * 深谷电价(￥/kwh)
     */
    @NotNull
    private BigDecimal deepPrice;

}
