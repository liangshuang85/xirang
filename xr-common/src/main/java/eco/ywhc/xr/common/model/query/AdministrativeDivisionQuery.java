package eco.ywhc.xr.common.model.query;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 行政区划
 */
@Getter
@Setter
@ToString
public class AdministrativeDivisionQuery {

    /**
     * 上级行政区的代码，默认值为100000，代表中国
     */
    @NotNull
    private Long parent = 100000L;

    /**
     * 包含下级行政区域的深度
     */
    @NotNull
    private Integer depth = 0;

}
