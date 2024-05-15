package eco.ywhc.xr.common.model.dto.req;

import lombok.Data;
import org.sugar.crud.model.BaseRestRequest;

import java.math.BigDecimal;

/**
 * 用户企业液氧/绿氢使用量Req
 */
@Data
public class OxygenHydrogenUsageReq implements BaseRestRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 关联ID
     */
    private Long refId;

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
