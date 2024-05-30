package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import eco.ywhc.xr.common.constant.TaskStatusType;
import eco.ywhc.xr.common.constant.TaskType;
import lombok.*;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;
import java.time.OffsetDateTime;

/**
 * 任务
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@TableName(value = "b_task")
public class Task extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 6514750159749147312L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 实例角色ID
     */
    private Long instanceRoleId;

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

    /**
     * 任务清单Guid
     */
    private String tasklistGuid;

    /**
     * 任务清单分组Guid
     */
    private String sectionGuid;

    /**
     * 任务负责人
     */
    private String members;

    /**
     * 任务URL
     */
    private String url;

    /**
     * 任务名
     */
    private String summary;

    /**
     * 任务发起时间
     */
    private OffsetDateTime startTime;

    /**
     * 任务结束时间
     */
    private OffsetDateTime endTime;

}
