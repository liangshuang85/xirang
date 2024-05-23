package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.ApprovalTemplateRefType;
import eco.ywhc.xr.common.constant.ApprovalType;
import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

import java.time.OffsetDateTime;

/**
 * 审批模板Res
 */
@Data
public class ApprovalTemplateRes implements BaseRestResponse {

    /**
     * 审批模板ID
     */
    private Long id;

    /**
     * 审批定义ID
     */
    private String approvalCode;

    /**
     * 审批模板部门
     */
    private String department;

    /**
     * 审批模板类型
     */
    private ApprovalType type;

    /**
     * 审批模板关联类型
     */
    private ApprovalTemplateRefType refType;

    /**
     * 创建时间
     */
    private OffsetDateTime createdAt;

    /**
     * 最后修改时间
     */
    private OffsetDateTime updatedAt;

}
