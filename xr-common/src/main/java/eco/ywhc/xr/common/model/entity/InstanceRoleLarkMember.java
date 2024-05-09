package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import eco.ywhc.xr.common.constant.InstanceRefType;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 角色成员
 */
@Getter
@Setter
@TableName("s_instance_role_lark_member")
public class InstanceRoleLarkMember extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = -5246463498518379053L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 实例角色ID
     */
    private Long instanceRoleId;

    /**
     * 关联对象ID
     */
    private Long refId;

    /**
     * 关联对象类型
     */
    private InstanceRefType refType;

    /**
     * 成员在飞书中的ID
     */
    private String memberId;

    /**
     * 成员类型
     */
    private String memberType;

}
