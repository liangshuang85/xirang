package eco.ywhc.xr.common.model.dto.res;

import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

/**
 * 测算数据时段表返回实体类
 */
@Data
public class MeasurementDataTimeRes implements BaseRestResponse {

    /**
     * 月份
     */
    private Integer month;

    /**
     * 尖峰时段
     */
    private String top;

    /**
     * 高峰时段
     */
    private String peak;

    /**
     * 平段时段
     */
    private String normal;

    /**
     * 低谷时段
     */
    private String valley;

    /**
     * 深谷时段
     */
    private String deep;

}
