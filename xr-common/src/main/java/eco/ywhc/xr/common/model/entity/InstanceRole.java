package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 实例角色
 */
@Getter
@Setter
@TableName("s_instance_role")
public class InstanceRole extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = -8055059165235076720L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 启用状态
     */
    private Boolean enabled;

    /**
     * 是否内置
     */
    private Boolean builtIn = false;

}
