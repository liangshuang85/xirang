package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 角色
 */
@Getter
@Setter
@ToString
@TableName("s_role")
public class Role extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 6578020000949674318L;

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 启用状态
     */
    private Boolean enabled = true;

    /**
     * 是否内置
     */
    private Boolean builtIn = false;

}

