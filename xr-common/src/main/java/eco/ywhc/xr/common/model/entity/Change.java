package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import eco.ywhc.xr.common.constant.InstanceRefType;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;
import java.time.OffsetDateTime;

/**
 * 变更记录
 */
@Getter
@Setter
@TableName("b_change")
public class Change implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 3637197916409625159L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 关联ID
     */
    private Long refId;

    /**
     * 关联类型
     */
    private InstanceRefType refType;

    /**
     * 变更前状态
     */
    @TableField("`before`")
    private String before;

    /**
     * 变更后状态
     */
    @TableField("`after`")
    private String after;

    /**
     * 变更耗时
     */
    private Integer elapsedDays;

    /**
     * 操作人的飞书OpenID
     */
    private String operatorId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    /**
     * 创建者
     */
    private Long createdBy;

}
