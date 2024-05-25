package eco.ywhc.xr.common.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

/**
 * 行政区划Query
 */
@Value
public class AdministrativeDivisionQuery {

    /**
     * 上级行政区的代码，默认值为100000，代表中国
     */
    @NotNull
    Long parent = 100000L;

    /**
     * 包含下级行政区域的深度
     */
    @NotNull
    Integer depth = 0;

}
