package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.ApprovalStatusType;
import eco.ywhc.xr.common.constant.ApprovalType;
import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 审批信息Res
 */
@Data
public class ApprovalRes implements BaseRestResponse {

    /**
     * 审批ID
     */
    private Long id;

    /**
     * 审批类型
     */
    private ApprovalType type;

    /**
     * 审批实例 Code
     */
    private String approvalInstanceId;

    /**
     * 审批状态
     */
    private ApprovalStatusType approvalStatus;

    /**
     * 待审批人
     */
    private List<AssigneeRes> pendingAssignees = new ArrayList<>();

    /**
     * 审批人
     */
    private List<AssigneeRes> assignees = new ArrayList<>();

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 审批单跳转地址
     */
    private List<String> appLinks = new ArrayList<>();

    /**
     * 审批发起时间
     */
    private OffsetDateTime startTime;

    /**
     * 审批结束时间
     */
    private OffsetDateTime endTime;

}
