package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.BaseRestRequest;

import java.util.HashSet;
import java.util.Set;

/**
 * 角色Request
 */
@Getter
@Setter
@ToString
public class RoleReq implements BaseRestRequest {

    /**
     * 名称
     */
    @NotBlank
    @Size(max = 64)
    private String name;

    /**
     * 描述
     */
    @Size(max = 128)
    private String description;

    /**
     * 权限编码集合
     */
    private Set<String> permissionCodes = new HashSet<>();

}

