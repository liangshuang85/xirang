package eco.ywhc.xr.common.model.query;

import lombok.Value;
import org.sugar.crud.query.BasePageQuery;

/**
 * 角色Query
 */
@Value
public class RoleQuery extends BasePageQuery {

    /**
     * 启用状态
     */
    Boolean enabled = true;

    /**
     * 包括负责人实例角色
     */
    Boolean includeBasic = false;

}
