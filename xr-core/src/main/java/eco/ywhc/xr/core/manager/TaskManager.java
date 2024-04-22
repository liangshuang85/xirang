package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.res.TaskRes;
import eco.ywhc.xr.common.model.entity.Task;
import org.springframework.lang.NonNull;

import java.util.List;

public interface TaskManager {

    /**
     * 根据关联ID获取最新的任务列表
     *
     * @param id 关联ID
     */
    List<Task> listTasksByRefId(long id);

    /**
     * 根据关联ID删除任务
     *
     * @param refId 关联ID
     */
    void logicDeleteEntityById(long refId);

    TaskRes getLarkTask(Task task)  ;

    Task findEntityById(@NonNull Long id);

}
