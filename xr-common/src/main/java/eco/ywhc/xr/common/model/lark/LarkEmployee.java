package eco.ywhc.xr.common.model.lark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 飞书员工
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LarkEmployee implements Serializable {

    @Serial
    private static final long serialVersionUID = 4924312611944440355L;

    /**
     * 用户的union_id
     */
    private String unionId;

    /**
     * 员工的用户ID
     */
    private String userId;

    /**
     * 用户的open_id
     */
    private String openId;

    /**
     * 中文姓名
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 部门的飞书department_id
     */
    private String[] departmentIds;

    /**
     * 工号
     */
    private String employeeNo;

    /**
     * 用户头像信息
     */
    private LarkAvatarInfo avatarInfo;

}
