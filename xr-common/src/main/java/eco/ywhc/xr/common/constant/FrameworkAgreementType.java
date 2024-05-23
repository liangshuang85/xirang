package eco.ywhc.xr.common.constant;

import eco.ywhc.xr.common.annotation.DictionaryEntryConstant;

/**
 * 框架协议项目类型
 */
@DictionaryEntryConstant
public enum FrameworkAgreementType {

    /**
     * 预立项
     */
    PRE_PROJECT,

    /**
     * 项目建议书拟定
     */
    PROJECT_PROPOSAL_DRAFTING,

    /**
     * 项目建议书内审
     */
    PROJECT_PROPOSAL_INTERNAL_REVIEW,

    /**
     * 政府审批
     */
    GOVERNMENT_APPROVAL,

    /**
     * 政府审批打回
     */
    GOVERNMENT_APPROVAL_REFUSED,

    /**
     * 框架协议拟定
     */
    FRAMEWORK_AGREEMENT_DRAFTING,

    /**
     * 待立项会
     */
    PENDING_PROJECT_MEETING,

    /**
     * 框架协议内审
     */
    FRAMEWORK_AGREEMENT_INTERNAL_REVIEW,

    /**
     * 框架协议签订
     */
    FRAMEWORK_AGREEMENT_SIGNING,

}
