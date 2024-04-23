package eco.ywhc.xr.common.constant;

import eco.ywhc.xr.common.annotation.DictionaryEntryConstant;

@DictionaryEntryConstant
public enum ApprovalStatusType {
    /**
     * 未审批
     */
    NOT_APPROVED,
    /**
     * 审批通过
     */
    APPROVED,
    /**
     * 审批未通过
     */
    REJECTED,
    /**
     * 未发起审批
     */
    NOT_INIT_APPROVAL
}
