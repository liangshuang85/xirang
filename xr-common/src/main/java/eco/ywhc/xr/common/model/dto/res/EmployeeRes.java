package eco.ywhc.xr.common.model.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import eco.ywhc.xr.common.model.lark.LarkAvatarInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 员工
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRes {

    /**
     * 用户的飞书OpenID
     */
    private String larkOpenId;

    /**
     * 中文姓名
     */
    private String name;

    /**
     * 用户头像信息
     */
    private LarkAvatarInfo avatarInfo;

    /**
     * 用户所分配的角色
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<RoleRes> roles;

}
