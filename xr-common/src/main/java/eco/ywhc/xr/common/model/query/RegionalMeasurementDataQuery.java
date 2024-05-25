package eco.ywhc.xr.common.model.query;

import lombok.Value;
import org.sugar.crud.query.BasePageQuery;

/**
 * 区域测算数据Query
 */
@Value
public class RegionalMeasurementDataQuery extends BasePageQuery {

    /**
     * 区域编码
     */
    Long adcode;

}
