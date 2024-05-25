package eco.ywhc.xr.common.model.query;

import eco.ywhc.xr.common.constant.InstanceRefType;
import lombok.Value;
import org.sugar.crud.query.BasePageQuery;

/**
 * 实例角色Query
 */
@Value
public class InstanceRoleQuery extends BasePageQuery {

    /**
     * 实例关联类型
     */
    InstanceRefType instanceRefType;

    /**
     * 启用状态
     */
    Boolean enabled = true;

    /**
     * 包括负责人实例角色
     */
    Boolean includeAssignee = false;

}
