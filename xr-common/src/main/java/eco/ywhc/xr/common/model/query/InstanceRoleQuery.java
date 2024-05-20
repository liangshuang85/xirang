package eco.ywhc.xr.common.model.query;

import eco.ywhc.xr.common.constant.InstanceRefType;
import lombok.Getter;
import lombok.ToString;
import org.sugar.crud.query.BasePageQuery;

/**
 * 角色Query
 */
@Getter
@ToString
public class InstanceRoleQuery extends BasePageQuery {

    /**
     * 实例关联类型
     */
    private InstanceRefType instanceRefType;

    /**
     * 启用状态
     */
    private Boolean enabled = true;

}
