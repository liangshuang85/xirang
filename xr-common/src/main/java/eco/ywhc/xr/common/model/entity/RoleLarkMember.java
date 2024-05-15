package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import eco.ywhc.xr.common.constant.RoleMemberType;
import lombok.*;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 角色成员
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "s_role_lark_member")
public class RoleLarkMember extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = -8385212579595086201L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 用户ID
     */
    private Long memberId;

    /**
     * 角色成员类型
     */
    private RoleMemberType memberType;

    /**
     * 项目ID
     */
    private Long projectId;

}
