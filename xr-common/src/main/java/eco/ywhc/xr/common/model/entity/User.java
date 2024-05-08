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
 * 用户
 */
@Getter
@Setter
@ToString
@TableName(value = "s_user")
public class User extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = -9110684463597520793L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 飞书用户的OpenID
     */
    private String larkOpenId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码哈希
     */
    private String passwordHash;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 全名
     */
    private String fullName;

    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * Email
     */
    private String email;

    /**
     * 是否为管理员
     */
    private Boolean admin;

    /**
     * 封禁标记
     */
    private Boolean blocked;

}
