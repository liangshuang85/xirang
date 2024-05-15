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

/**
 * 用户企业液氧/绿氢使用量
 */
@Getter
@Setter
@ToString
@TableName("b_oxygen_hydrogen_usage")
public class OxygenHydrogenUsage extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 5478795674677306737L;

    @TableId(type = IdType.ASSIGN_ID)
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
