package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.TaskStatusType;
import eco.ywhc.xr.common.constant.TaskType;
import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

/**
 * 任务Res
 */
@Data
public class TaskRes implements BaseRestResponse {

    /**
     * 任务ID
     */
    private Long id;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 任务的完成时刻时间戳
     */
    private String completedAt;

    /**
     * 任务URL
     */
    private String taskUrl;

    /**
     * 任务名
     */
    private String summary;

    /**
     * 任务状态
     */
    private TaskStatusType status;

    /**
     * 任务类别
     */
    private TaskType type;

    /**
     * 负责人信息
     */
    private AssigneeRes assignee;

}
