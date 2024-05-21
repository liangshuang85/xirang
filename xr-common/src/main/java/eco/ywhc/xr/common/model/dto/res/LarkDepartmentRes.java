package eco.ywhc.xr.common.model.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import eco.ywhc.xr.common.constant.RoleMemberType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * 部门Response
 */
@Getter
@Setter
@ToString
public class LarkDepartmentRes {

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 父部门ID
     */
    private String parentDepartmentId;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门主管OpenId
     */
    private String leaderUserId;

    /**
     * 部分所分配的角色
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<RoleMemberType, List<RoleRes>> roles;

}
