package eco.ywhc.xr.common.model.dto.res;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 角色成员Res
 */
@Data
public class InstanceRoleLarkMemberRes {

    /**
     * ID
     */
    private Long id;

    /**
     * 实例角色ID
     */
    private Long instanceRoleId;

    /**
     * 成员在飞书中的ID
     */
    private Set<String> memberIds = new HashSet<>();

}
