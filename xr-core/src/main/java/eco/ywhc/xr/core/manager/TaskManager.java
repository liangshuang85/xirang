package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.res.DepartmentRes;
import eco.ywhc.xr.common.model.dto.res.TaskRes;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.common.model.entity.TaskTemplate;
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

    /**
     * 获取飞书任务
     */
    TaskRes getLarkTask(Task task);

    /**
     * 根据ID获取任务模板
     *
     * @param id 任务模板ID
     */
    TaskTemplate getTaskTemplateById(long id);

    /**
     * 根据部门ID从飞书获取部门信息
     *
     * @param departmentId 部门ID
     */
    DepartmentRes getDepartmentByDepartmentId(@NonNull String departmentId);

    Task findEntityById(@NonNull Long id);

}
