package eco.ywhc.xr.common.model.dto.res;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 状态树
 *
 * @param <R> 状态类型
 */
@Data
public class StatusTree<R> {

    /**
     * 状态
     */
    private R status;

    /**
     * 当前状态的所有子状态
     */
    private List<StatusTree<R>> children = new ArrayList<>();

}
