package eco.ywhc.xr.common.model.query;

import eco.ywhc.xr.common.constant.ClueLevel;
import eco.ywhc.xr.common.constant.ClueStatusType;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.sugar.crud.query.BasePageQuery;

/**
 * 线索Query
 */
@Value
public class ClueQuery extends BasePageQuery {

    /**
     * 行政区代码
     */
    Long adcode;

    /**
     * 线索状态
     */
    ClueStatusType status;

    /**
     * 负责人
     */
    @Size(max = 255)
    String assigneeId;

    /**
     * 线索评级
     */
    ClueLevel level;

}
