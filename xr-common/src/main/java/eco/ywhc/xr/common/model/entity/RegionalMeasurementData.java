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
 * 区域测算数据表实体类
 */
@Getter
@Setter
@ToString
@TableName(value = "b_regional_measurement_data")
public class RegionalMeasurementData extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 2360439552003213574L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 6位数字格式的行政区划代码
     */
    private Long adcode;

    /**
     * 充电价格(￥/kwh)
     */
    private BigDecimal chargingPrice;

    /**
     * 充电补贴(￥/kWh)
     */
    private BigDecimal chargingSubsidy;

    /**
     * 放电价格(￥/kwh)
     */
    private BigDecimal dischargingPrice;

    /**
     * 租赁价格(￥/kwh·年)
     */
    private BigDecimal leasingPrice;

    /**
     * 碳排放因子(tCO:/MWh)
     */
    private BigDecimal carbonEmissionFactor;

    /**
     * 增值税%
     */
    private BigDecimal vat;

    /**
     * 城建税%
     */
    private BigDecimal urbanConstructionTax;

    /**
     * 教育费%
     */
    private BigDecimal educationFee;

    /**
     * 所得税%
     */
    private BigDecimal incomeTax;

    /**
     * 碳资产增值税%
     */
    private BigDecimal carbonAssetValueAddedTax;

    /**
     * 尖峰电价(￥ /kwh)
     */
    private BigDecimal topPrice;

    /**
     * 平段电价(￥/kwh)
     */
    private BigDecimal normalPrice;

    /**
     * 低谷电价(￥/kWh)
     */
    private BigDecimal valleyPrice;

    /**
     * 线损电价(￥/kWh)
     */
    private BigDecimal lineLossPrice;

    /**
     * 系统运行费用(￥ /kWh)
     */
    private BigDecimal systemOperationCost;

    /**
     * 高峰电价(￥ /kwh)
     */
    private BigDecimal peakPrice;

    /**
     * 深谷电价(￥/kwh)
     */
    private BigDecimal deepPrice;

}
