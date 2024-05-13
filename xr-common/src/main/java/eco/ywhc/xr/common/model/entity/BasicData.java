package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@TableName(value = "b_basic_data")
public class BasicData extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 4236327238059223474L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 关联ID
     */
    private Long refId;

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
