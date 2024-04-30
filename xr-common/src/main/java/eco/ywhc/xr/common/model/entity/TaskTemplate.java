package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import eco.ywhc.xr.common.constant.TaskTemplateRefType;
import eco.ywhc.xr.common.constant.TaskType;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 任务模板
 */
@Getter
@Setter
@TableName(value = "b_task_template")
public class TaskTemplate extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 6728582666958999546L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 任务模板部门ID
     */
    private String departmentId;

    /**
     * 任务模板类型
     */
    private TaskType type;

    /**
     * 任务模板关联类型
     */
    private TaskTemplateRefType refType;

}
