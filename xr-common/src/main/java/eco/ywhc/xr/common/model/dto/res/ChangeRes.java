package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.InstanceRefType;
import lombok.Data;

/**
 * 变更记录Res
 */
@Data
public class ChangeRes {


    /**
     * 关联ID
     */
    private Long refId;

    /**
     * 关联类型
     */
    private InstanceRefType refType;

    /**
     * 变更前状态
     */
    private String before;

    /**
     * 变更后状态
     */
    private String after;

    /**
     * 变更耗时
     */
    private Integer elapsedDays;

}
