package eco.ywhc.xr.common.model;

import lombok.Data;

import java.util.List;

/**
 * 任务清单信息
 */
@Data
public class TaskListInfo {

    /**
     * 任务清单Guid
     */
    private String taskListGuid;

    /**
     * 任务清单成员
     */
    private List<String> members;

}
