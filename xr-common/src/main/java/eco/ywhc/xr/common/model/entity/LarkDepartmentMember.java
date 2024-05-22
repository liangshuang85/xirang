package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 飞书部门成员信息
 */
@Data
@TableName(value = "s_lark_department_member")
public class LarkDepartmentMember {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 员工OpenId
     */
    private String employeeOpenId;

}
