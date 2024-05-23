package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.TaskTemplateRefType;
import eco.ywhc.xr.common.constant.TaskType;
import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

import java.time.OffsetDateTime;

/**
 * 任务模板Res
 */
@Data
public class TaskTemplateRes implements BaseRestResponse {

    /**
     * 任务模板ID
     */
    private Long id;

    /**
     * 实例角色ID
     */
    private Long instanceRoleId;

    /**
     * 任务模板类型
     */
    private TaskType type;

    /**
     * 任务模板关联类型
     */
    private TaskTemplateRefType refType;

    /**
     * 创建时间
     */
    private OffsetDateTime createdAt;

    /**
     * 最后修改时间
     */
    private OffsetDateTime updatedAt;

}
