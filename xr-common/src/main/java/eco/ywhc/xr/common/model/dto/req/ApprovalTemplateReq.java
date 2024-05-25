package eco.ywhc.xr.common.model.dto.req;

import eco.ywhc.xr.common.constant.ApprovalTemplateRefType;
import eco.ywhc.xr.common.constant.ApprovalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 审批模板Req
 */
@Value
public class ApprovalTemplateReq implements BaseRestRequest {

    /**
     * 审批定义ID
     */
    @NotBlank
    @Size(max = 40)
    String approvalCode;

    /**
     * 审批模板部门
     */
    @NotBlank
    @Size(max = 40)
    String department;

    /**
     * 审批模板类型
     */
    @NotNull
    ApprovalType type;

    /**
     * 审批模板关联类型
     */
    @NotNull
    ApprovalTemplateRefType refType;

}
