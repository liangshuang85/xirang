package eco.ywhc.xr.common.model.dto.req;

import eco.ywhc.xr.common.constant.InstanceRefType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.util.HashSet;
import java.util.Set;

/**
 * 实例角色
 */
@Value
public class InstanceRoleReq implements BaseRestRequest {

    /**
     * 角色名
     */
    @NotBlank
    @Size(max = 64)
    String name;

    /**
     * 角色描述
     */
    @Size(max = 128)
    String description;

    /**
     * 关联对象类型
     */
    @NotNull
    InstanceRefType refType;

    /**
     * 是否为负责人
     */
    @NotNull
    Boolean assignee = false;

    /**
     * 权限编码集合
     */
    @NotNull
    Set<String> permissionCodes = new HashSet<>();

}
