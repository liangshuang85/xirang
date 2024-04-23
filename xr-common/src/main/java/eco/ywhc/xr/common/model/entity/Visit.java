package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;
import java.time.LocalDate;

/**
 * 拜访记录
 */
@Getter
@Setter
@TableName("b_visit")
public class Visit extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = -6903018664054644646L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 线索id
     */
    private Long clueId;

    /**
     * 是否为正式拜访
     */
    private Boolean official;

    /**
     * 拜访日期
     */
    private LocalDate visitDate;

    /**
     * 访问类型
     */
    private String type = "";

}

