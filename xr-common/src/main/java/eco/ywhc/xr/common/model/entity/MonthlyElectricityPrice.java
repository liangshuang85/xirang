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
 * 月度电价表实体类
 */
@Getter
@Setter
@ToString
@TableName(value = "b_monthly_electricity_price")
public class MonthlyElectricityPrice extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = -5572927437316750485L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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
