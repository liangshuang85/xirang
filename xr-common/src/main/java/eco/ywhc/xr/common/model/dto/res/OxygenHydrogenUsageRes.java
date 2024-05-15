package eco.ywhc.xr.common.model.dto.res;

import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

import java.math.BigDecimal;

/**
 * 用户企业液氧/绿氢使用量Res
 */
@Data
public class OxygenHydrogenUsageRes implements BaseRestResponse {

    /**
     * ID
     */
    private Long id;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 年均液氧使用量(t/年)
     */
    private BigDecimal annualLiquidOxygenUsage;

    /**
     * 年均绿氢使用量(t/年)
     */
    private BigDecimal annualGreenHydrogenUsage;

}
