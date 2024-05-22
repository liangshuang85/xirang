package eco.ywhc.xr.common.model.query;

import lombok.Getter;
import lombok.ToString;
import org.sugar.crud.query.BasePageQuery;

/**
 * 角色Query
 */
@Getter
@ToString
public class RoleQuery extends BasePageQuery {

    /**
     * 启用状态
     */
    private Boolean enabled = true;

    /**
     * 包括负责人实例角色
     */
    private Boolean includeBasic = false;

}
