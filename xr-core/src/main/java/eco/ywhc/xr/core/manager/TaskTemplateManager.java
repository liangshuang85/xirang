package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.constant.TaskTemplateRefType;
import eco.ywhc.xr.common.constant.TaskType;
import eco.ywhc.xr.common.model.entity.TaskTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = {Exception.class})
public interface TaskTemplateManager {

    /**
     * 根据类型获取任务模板列表
     *
     * @param refType 任务模板关联类型
     * @param type    任务模板类型
     */
    List<TaskTemplate> listByType(@NonNull TaskTemplateRefType refType, @NonNull TaskType type);

    /**
     * 查找指定任务模板
     *
     * @param id 任务模板ID
     */
    TaskTemplate findEntityById(long id);

    /**
     * 查找指定任务模板，没有找到则抛出异常
     *
     * @param id 任务模板ID
     */
    TaskTemplate mustFindEntityById(long id);

}
