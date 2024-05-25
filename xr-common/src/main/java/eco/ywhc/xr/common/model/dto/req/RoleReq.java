package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.util.HashSet;
import java.util.Set;

/**
 * 角色Request
 */
@Value
public class RoleReq implements BaseRestRequest {

    /**
     * 名称
     */
    @NotBlank
    @Size(max = 64)
    String name;

    /**
     * 描述
     */
    @Size(max = 128)
    String description;

    /**
     * 权限编码集合
     */
    Set<String> permissionCodes = new HashSet<>();

}

