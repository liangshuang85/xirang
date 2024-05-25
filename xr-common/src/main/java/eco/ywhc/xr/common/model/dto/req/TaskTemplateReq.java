package eco.ywhc.xr.common.model.dto.req;

import eco.ywhc.xr.common.constant.TaskTemplateRefType;
import eco.ywhc.xr.common.constant.TaskType;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 任务模板Req
 */
@Value
public class TaskTemplateReq implements BaseRestRequest {

    /**
     * 实例角色ID
     */
    @NotNull
    Long instanceRoleId;

    /**
     * 任务模板类型
     */
    @NotNull
    TaskType type;

    /**
     * 任务模板关联类型
     */
    @NotNull
    TaskTemplateRefType refType;

}
