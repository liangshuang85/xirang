package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import eco.ywhc.xr.common.constant.ApprovalTemplateRefType;
import eco.ywhc.xr.common.constant.ApprovalType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;
import java.util.Set;

/**
 * 审批模板
 */
@Getter
@Setter
@ToString
@TableName(value = "b_approval_template", autoResultMap = true)
public class ApprovalTemplate extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 4652509279886639110L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 部门名称
     */
    private String department;

    /**
     * 负责人的飞书OpenID列表
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<String> assigneeIds;

    /**
     * 审批类型
     */
    private ApprovalType type;

    /**
     * 审批模板关联类型
     */
    private ApprovalTemplateRefType refType;

}
