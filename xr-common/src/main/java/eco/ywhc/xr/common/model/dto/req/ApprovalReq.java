package eco.ywhc.xr.common.model.dto.req;

import eco.ywhc.xr.common.constant.ApprovalStatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 审批信息Req
 */
@Data
public class ApprovalReq implements BaseRestRequest {

    /**
     * 关联对象ID
     */
    private Long refId;

    /**
     * 部门名称
     */
    @Size(max = 100)
    private String departmentName;

    /**
     * 审批单编号或引用
     */
    @NotBlank
    @Size(max = 255)
    private String approvalDocument;

    /**
     * 审批状态
     */
    @NotNull
    private ApprovalStatusType approvalStatus;

}
