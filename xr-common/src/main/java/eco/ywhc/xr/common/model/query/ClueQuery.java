package eco.ywhc.xr.common.model.query;

import eco.ywhc.xr.common.constant.ClueStatusType;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.query.BasePageQuery;

@Getter
@Setter
@ToString
public class ClueQuery extends BasePageQuery {

    /**
     * 行政区代码
     */
    private Long adcode;

    /**
     * 线索状态
     */
    private ClueStatusType status;

    /**
     * 负责人
     */
    @Size(max = 255)
    private String assigneeId;

}
