package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.InstanceRefType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.IdentifiableResponse;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 实例角色
 */
@Getter
@Setter
@ToString
public class InstanceRoleRes implements IdentifiableResponse<Long>, Serializable {

    @Serial
    private static final long serialVersionUID = -8250277521984655445L;

    /**
     * ID
     */
    private Long id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 关联对象类型
     */
    private InstanceRefType refType;

    /**
     * 是否为负责人
     */
    private Boolean assignee;

    /**
     * 启用状态
     */
    private Boolean enabled;

    /**
     * 权限编码集合
     */
    private List<PermissionRes> permissions = new ArrayList<>();

    /**
     * 是否内置
     */
    private Boolean builtIn = false;

    /**
     * 创建时间
     */
    private OffsetDateTime createdAt;

    /**
     * 最后修改时间
     */
    private OffsetDateTime updatedAt;

}
