package eco.ywhc.xr.common.model.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.query.BasePageQuery;

/**
 * 区域测算数据查询
 */
@Getter
@Setter
@ToString
public class RegionalMeasurementDataQuery extends BasePageQuery {

    /**
     * 区域编码
     */
    private Long adcode;

}
