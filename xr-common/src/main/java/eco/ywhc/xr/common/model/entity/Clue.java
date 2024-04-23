package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import eco.ywhc.xr.common.constant.ClueLevel;
import eco.ywhc.xr.common.constant.ClueStatusType;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 基础线索信息
 */
@Getter
@Setter
@TableName("b_clue")
public class Clue extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = -2081075227262400491L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 行政区代码
     */
    private Long adcode;

    /**
     * 线索状态
     */
    private ClueStatusType status;

    /**
     * 线索级别
     */
    private ClueLevel level;

    /**
     * 线索编号
     */
    private String clueCode;

    /**
     * 负责人
     */
    private String assigneeId;


}

