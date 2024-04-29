package eco.ywhc.xr.common.constant;

import eco.ywhc.xr.common.annotation.DictionaryEntryConstant;

/**
 * 项目状态类型
 */
@DictionaryEntryConstant
public enum ProjectStatusType {

    /**
     * 新建
     */
    NEW,

    /**
     * 投资协议拟定
     */
    INVESTMENT_AGREEMENT_DRAFTING,

    /**
     * 投资协议内审
     */
    INVESTMENT_AGREEMENT_INTERNAL_REVIEW,

    /**
     * 待投决
     */
    PENDING_DECISION,

    /**
     * 投资协议终审
     */
    INVESTMENT_AGREEMENT_FINAL_REVIEW,

    /**
     * 投资协议签订
     */
    INVESTMENT_AGREEMENT_SIGNING,

    /**
     * 企业备案中
     */
    ENTERPRISE_FILING,

    /**
     * 备案完成
     */
    FILING_COMPLETED,

}
