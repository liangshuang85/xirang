package eco.ywhc.xr.common.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 部门Res
 */
@Data
@AllArgsConstructor(staticName = "of")
public class DepartmentRes {

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门主管ID
     */
    private String leaderUserId;

}
