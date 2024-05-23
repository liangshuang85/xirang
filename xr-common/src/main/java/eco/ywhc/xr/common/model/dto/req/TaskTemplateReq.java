package eco.ywhc.xr.common.model.dto.req;

import eco.ywhc.xr.common.constant.TaskTemplateRefType;
import eco.ywhc.xr.common.constant.TaskType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 任务模板Req
 */
@Data
public class TaskTemplateReq implements BaseRestRequest {

    /**
     * 实例角色ID
     */
    @NotNull
    private Long instanceRoleId;

    /**
     * 任务模板类型
     */
    @NotNull
    private TaskType type;

    /**
     * 任务模板关联类型
     */
    @NotNull
    private TaskTemplateRefType refType;

}
