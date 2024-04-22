package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.constant.TaskTemplateRefType;
import eco.ywhc.xr.common.constant.TaskType;
import eco.ywhc.xr.common.model.entity.TaskTemplate;
import org.springframework.lang.NonNull;

import java.util.List;

public interface TaskTemplateManager {

    /**
     * 根据类型获取任务模板列表
     *
     * @param refType 任务模板关联类型
     * @param type    任务模板类型
     */
    List<TaskTemplate> listByType(@NonNull TaskTemplateRefType refType, @NonNull TaskType type);

}
