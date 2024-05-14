package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 测算数据时段表实体类
 */
@Getter
@Setter
@ToString
@TableName(value = "b_measurement_data_time")
public class MeasurementDataTime extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = -1254948826803309598L;

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
     * 尖峰时段
     */
    private String top;

    /**
     * 高峰时段
     */
    private String peak;

    /**
     * 平段时段
     */
    private String normal;

    /**
     * 低谷时段
     */
    private String valley;

    /**
     * 深谷时段
     */
    private String deep;

}
