package eco.ywhc.xr.common.model.dto.res;

import lombok.Data;

import java.time.LocalDate;

/**
 * 拜访信息Res
 */
@Data
public class VisitRes {

    /**
     * 拜访信息ID
     */
    private Long id;

    /**
     * 是否为正式拜访
     */
    private Boolean official;

    /**
     * 拜访日期
     */
    private LocalDate visitDate;

}
