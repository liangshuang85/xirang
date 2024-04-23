package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import eco.ywhc.xr.common.constant.ApprovalStatusType;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 审批记录
 */
@Getter
@Setter
@TableName("b_approval")
public class Approval extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = -7935396404069590664L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 关联对象ID
     */
    private Long refId;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 负责人的飞书OpenID列表
     */
    private Set<String> assigneeIds;

    /**
     * 审批单编号或引用
     */
    private String approvalDocument;

    /**
     * 审批状态
     */
    private ApprovalStatusType approvalStatus;

    /**
     * 审批时间
     */
    private LocalDateTime creationDate;

}
