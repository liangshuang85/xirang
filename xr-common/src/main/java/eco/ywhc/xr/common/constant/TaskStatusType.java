package eco.ywhc.xr.common.constant;

import eco.ywhc.xr.common.annotation.DictionaryEntryConstant;

/**
 * 任务状态类型
 */
@DictionaryEntryConstant
public enum TaskStatusType {

    /**
     * 待发起
     */
    pending,
    /**
     * 未完成
     */
    todo,
    /**
     * 已完成
     */
    done,
    /**
     * 已删除
     */
    deleted,

}
