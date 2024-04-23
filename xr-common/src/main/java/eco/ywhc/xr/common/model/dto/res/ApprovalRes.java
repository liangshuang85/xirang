package eco.ywhc.xr.common.model.dto.res;


import eco.ywhc.xr.common.constant.ApprovalStatusType;
import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

/**
 * 审批信息Res
 */
@Data
public class ApprovalRes implements BaseRestResponse {

    /**
     * 审批信息ID
     */
    private Long id;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 负责人的飞书OpenID
     */
    private String assigneeId;

    /**
     * 审批单编号或引用
     */
    private String approvalDocument;

    /**
     * 审批状态
     */
    private ApprovalStatusType approvalStatus;

}
