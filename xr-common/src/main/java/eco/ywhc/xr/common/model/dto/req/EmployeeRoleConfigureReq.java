package eco.ywhc.xr.common.model.dto.req;

import eco.ywhc.xr.common.constant.RoleMemberType;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 配置员工角色的请求
 */
@Value
public class EmployeeRoleConfigureReq implements BaseRestRequest {

    /**
     * 角色ID集合
     */
    @NotNull
    Map<RoleMemberType, Set<Long>> roles = new HashMap<>();

}
