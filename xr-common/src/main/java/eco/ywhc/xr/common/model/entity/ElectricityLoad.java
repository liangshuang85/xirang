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
 * 用电企业负荷相关信息
 */
@Getter
@Setter
@ToString
@TableName("b_electricity_load")
public class ElectricityLoad extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 1227030299408215394L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 关联ID
     */
    private Long refId;

    /**
     * 用电企业名称
     */
    private String name;

    /**
     * 年用电量(kWh)
     */
    private BigDecimal annualElectricityUsage;

    /**
     * 电力负荷(kW) - 注意：单位应为kW，而不是kwh，除非有特殊含义
     */
    private BigDecimal electricityLoad;

    /**
     * 企业主要生产用电时间段
     */
    private String mainProductionTime;

    /**
     * 主要生产时间段用电量(kWh)
     */
    private BigDecimal mainProductionElectricityUsage;

    /**
     * 园区综合电价(￥/kWh)
     */
    private BigDecimal parkComprehensiveElectricityPrice;

}
