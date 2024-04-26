package eco.ywhc.xr.common.constant;

import eco.ywhc.xr.common.annotation.DictionaryEntryConstant;

@DictionaryEntryConstant
public enum ApprovalStatusType {
    /**
     * 未审批/审批中/待审批
     */
    PENDING,
    /**
     * 审批通过
     */
    APPROVED,
    /**
     * 审批未通过
     */
    REJECTED,
    /**
     * 撤回
     */
    CANCELED,
    /**
     * 删除
     */
    DELETED,
    /**
     * 未发起审批/待发起审批
     */
    PENDING_START,
}
