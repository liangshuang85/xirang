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
 * 行政区划
 */
@Getter
@Setter
@ToString
@TableName(value = "d_administrative_division")
public class AdministrativeDivision extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 5289472369853943763L;

    /**
     * 行政区划ID
     * <p>
     * 等于{@link AdministrativeDivision#adcode 行政区划代码}
     * </p>
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 行政区名称
     */
    private String name;

    /**
     * 6位数字格式的行政区划代码
     * <p>
     * 等于{@link AdministrativeDivision#id 行政区划ID}
     * </p>
     */
    private Long adcode;

    /**
     * 父级行政区划代码
     */
    private Long parent;

    /**
     * 行政区划级别：0为国家、1为省级、2为地市、3为区县
     */
    private Short level;

}
