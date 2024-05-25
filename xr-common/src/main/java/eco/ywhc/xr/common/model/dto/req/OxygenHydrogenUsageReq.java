package eco.ywhc.xr.common.model.dto.req;

import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.math.BigDecimal;

/**
 * 用户企业液氧/绿氢使用量Req
 */
@Value
public class OxygenHydrogenUsageReq implements BaseRestRequest {

    /**
     * ID
     */
    Long id;

    /**
     * 企业名称
     */
    String name;

    /**
     * 年均液氧使用量(t/年)
     */
    BigDecimal annualLiquidOxygenUsage;

    /**
     * 年均绿氢使用量(t/年)
     */
    BigDecimal annualGreenHydrogenUsage;

}
