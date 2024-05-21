package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 角色成员Req
 */
@Data
public class InstanceRoleLarkMemberReq {

    /**
     * 实例角色ID
     */
    @NotNull
    private Long instanceRoleId;

    /**
     * 成员在飞书中的ID
     */
    @NotNull
    @Size(max = 100)
    private Set<String> memberIds = new HashSet<>();

}
