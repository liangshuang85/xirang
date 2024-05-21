package eco.ywhc.xr.common.model.dto.req;

import eco.ywhc.xr.common.constant.RoleMemberType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.BaseRestRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 配置部门角色的请求
 */
@Getter
@Setter
@ToString
public class DepartmentRoleConfigureReq implements BaseRestRequest {

    /**
     * 角色ID集合
     */
    @NotNull
    private Map<RoleMemberType, Set<Long>> roles = new HashMap<>();

}
