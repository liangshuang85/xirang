package eco.ywhc.xr.common.constant;

import eco.ywhc.xr.common.annotation.DictionaryEntryConstant;

@DictionaryEntryConstant
public enum ClueStatusType {
    /**
     * 线索发现
     */
    CLUE_DISCOVERY,

    /**
     * 线索录入
     */
    CLUE_ENTRY,

    /**
     * 入库待评估
     */
    CLUE_STORAGE_EVALUATION,

    /**
     * 评估中
     */
    CLUE_EVALUATION,

    /**
     * 跟进中
     */
    CLUE_FOLLOW,

    /**
     * 可派生
     */
    CLUE_DERIVABLE,

    /**
     * 关闭
     */
    CLUE_CLOSE,

    /**
     * 可预立项
     */
    CLUE_PROPOSABLE,

    /**
     * 审批通过
     */
    CLUE_APPROVE,

}
