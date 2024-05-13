package eco.ywhc.xr.common.model.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务清单Req
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskListReq {

    /**
     * 任务清单Guid
     */
    private String taskListGuid;

    /**
     * 分组Guid
     */
    private String sectionGuid;

    /**
     * 任务Guid
     */
    private String taskGuid;

}
