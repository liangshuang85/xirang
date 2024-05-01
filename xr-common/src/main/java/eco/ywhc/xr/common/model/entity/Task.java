package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import eco.ywhc.xr.common.constant.TaskStatusType;
import eco.ywhc.xr.common.constant.TaskType;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 任务
 */
@Getter
@Setter
@TableName(value = "b_task")
public class Task extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 6514750159749147312L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 部门名称
     */
    private String department;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 任务类型
     */
    private TaskType type;

    /**
     * 任务的完成时刻时间戳
     */
    private String completedAt;

    /**
     * 飞书任务的GUID
     */
    private String taskGuid;

    /**
     * 关联对象ID
     */
    private Long refId;

    /**
     * 任务模板ID
     */
    private Long taskTemplateId;

    /**
     * 任务状态
     */
    private TaskStatusType status;

}
