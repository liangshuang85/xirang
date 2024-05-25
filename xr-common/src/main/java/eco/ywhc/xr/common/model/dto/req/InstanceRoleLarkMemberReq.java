package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.util.HashSet;
import java.util.Set;

/**
 * 角色成员Req
 */
@Value
public class InstanceRoleLarkMemberReq implements BaseRestRequest {

    /**
     * 实例角色ID
     */
    @NotNull
    Long instanceRoleId;

    /**
     * 成员在飞书中的ID
     */
    @NotNull
    @Size(max = 100)
    Set<String> memberIds = new HashSet<>();

}
