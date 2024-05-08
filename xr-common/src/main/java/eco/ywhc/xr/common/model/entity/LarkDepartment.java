package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 飞书部门信息
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@TableName(value = "s_lark_department")
public class LarkDepartment {

    /**
     * 部门ID
     */
    @TableId(type = IdType.ASSIGN_ID)
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

}
