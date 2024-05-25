package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.math.BigDecimal;

/**
 * 月度电价请求实体类
 */
@Value
public class MonthlyElectricityPriceReq implements BaseRestRequest {

    /**
     * 月份
     */
    @NotNull
    Integer month;

    /**
     * 尖峰电价(￥ /kwh)
     */
    @NotNull
    BigDecimal topPrice;

    /**
     * 高峰电价(￥ /kwh)
     */
    @NotNull
    BigDecimal peakPrice;

    /**
     * 平段电价(￥/kwh)
     */
    @NotNull
    BigDecimal normalPrice;

    /**
     * 低谷电价(￥/kWh)
     */
    @NotNull
    BigDecimal valleyPrice;

    /**
     * 深谷电价(￥/kwh)
     */
    @NotNull
    BigDecimal deepPrice;

}
