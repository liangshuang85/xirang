package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.constant.TaskTemplateRefType;
import eco.ywhc.xr.common.constant.TaskType;
import eco.ywhc.xr.common.model.dto.req.TaskListReq;
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
     * 获取未完成的任务列表
     *
     * @param prevTaskId 上一个任务ID
     * @param limit      限制数量
     */
    List<Task> listTasks(long prevTaskId, int limit);

    /**
     * 获取任务清单
     *
     * @param taskListGuid 任务清单Guid
     */
    List<String> getTaskList(String taskListGuid);

    /**
     * 创建任务清单
     *
     * @param refType 任务关联类型
     * @param name    任务关联对象名称
     */
    String createTaskList(TaskTemplateRefType refType, String name);

    /**
     * 创建任务清单分组
     *
     * @param taskListGuid 任务清单Guid
     * @param taskType     任务类型
     */
    String createTaskListSection(String taskListGuid, TaskType taskType);

    /**
     * 更新任务清单名称
     *
     * @param taskListGuid 任务清单Guid
     * @param name         任务清单名称
     */
    void updateTaskListName(String taskListGuid, TaskTemplateRefType type, String name);

    /**
     * 获取任务清单分组
     *
     * @param type         任务类型
     * @param taskListGuid 任务清单Guid
     */
    String getSections(TaskType type, String taskListGuid);

    /**
     * 添加任务到任务清单
     *
     * @param req 任务清单req
     */
    void addTaskToTaskList(TaskListReq req);

    /**
     * 添加成员到任务清单
     *
     * @param taskListGuid 任务清单Guid
     * @param memberIds    成员ID
     */
    void addMemberToTaskList(String taskListGuid, List<String> memberIds);

    /**
     * 从任务清单中移除成员
     *
     * @param taskListGuid 任务清单Guid
     * @param memberIds    成员ID
     */
    void removeMemberFromTaskList(String taskListGuid, List<String> memberIds);

    /**
     * 根据ID更新任务
     *
     * @param task 任务
     */
    void updateById(Task task);

    /**
     * 根据关联ID获取任意一个任务
     *
     * @param refId 关联ID
     */
    Task findAnyTaskByRefId(long refId);

    Task findEntityById(@NonNull Long id);

}
