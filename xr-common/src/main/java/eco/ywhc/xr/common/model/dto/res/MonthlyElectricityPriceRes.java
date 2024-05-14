package eco.ywhc.xr.common.model.dto.res;

import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

import java.math.BigDecimal;

/**
 * 月度电价表返回实体类
 */
@Data
public class MonthlyElectricityPriceRes implements BaseRestResponse {

    /**
     * 关联的区域测算数据表id
     */
    private Long refId;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 尖峰电价(￥ /kwh)
     */
    private BigDecimal topPrice;

    /**
     * 高峰电价(￥ /kwh)
     */
    private BigDecimal peakPrice;

    /**
     * 平段电价(￥/kwh)
     */
    private BigDecimal normalPrice;

    /**
     * 低谷电价(￥/kWh)
     */
    private BigDecimal valleyPrice;

    /**
     * 深谷电价(￥/kwh)
     */
    private BigDecimal deepPrice;

}
