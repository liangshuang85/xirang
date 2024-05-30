package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import eco.ywhc.xr.common.constant.ApprovalStatusType;
import eco.ywhc.xr.common.constant.ApprovalTemplateRefType;
import eco.ywhc.xr.common.constant.ApprovalType;
import lombok.*;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;
import java.time.OffsetDateTime;

/**
 * 审批记录
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
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
     * 关联对象类型
     */
    private ApprovalTemplateRefType refType;

    /**
     * 审批定义Code
     */
    private String approvalCode;

    /**
     * 审批类型
     */
    private ApprovalType type;

    /**
     * 审批实例Code
     */
    private String approvalInstanceId;

    /**
     * 审批状态
     */
    private ApprovalStatusType approvalStatus;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 审批成员的ID
     */
    private String members;

    /**
     * 审批任务ID
     */
    private String instanceTasks;

    /**
     * 审批发起时间
     */
    private OffsetDateTime startTime;

    /**
     * 审批结束时间
     */
    private OffsetDateTime endTime;

}
