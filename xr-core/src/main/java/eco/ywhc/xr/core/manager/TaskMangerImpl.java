package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lark.oapi.Client;
import com.lark.oapi.service.task.v2.model.*;
import eco.ywhc.xr.common.constant.TaskStatusType;
import eco.ywhc.xr.common.constant.TaskTemplateRefType;
import eco.ywhc.xr.common.constant.TaskType;
import eco.ywhc.xr.common.exception.LarkTaskNotFoundException;
import eco.ywhc.xr.common.model.TaskListInfo;
import eco.ywhc.xr.common.model.dto.req.TaskListReq;
import eco.ywhc.xr.common.model.dto.res.TaskRes;
import eco.ywhc.xr.common.model.entity.BaseEntity;
import eco.ywhc.xr.common.model.entity.InstanceRole;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.common.model.entity.TaskTemplate;
import eco.ywhc.xr.core.mapper.TaskMapper;
import eco.ywhc.xr.core.mapper.TaskTemplateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskMangerImpl implements TaskManager {

    private static final String prefix = "息壤-";

    private static final String resourceType = "tasklist";

    private final TaskMapper taskMapper;

    private final TaskTemplateMapper taskTemplateMapper;

    private final Client client;

    private final InstanceRoleManager instanceRoleManager;

    @Override
    public List<Task> listTasksByRefId(long id) {
        QueryWrapper<Task> qw = new QueryWrapper<>();
        qw.lambda().eq(BaseEntity::getDeleted, 0)
                .eq(Task::getRefId, id)
                .orderByDesc(Task::getId);
        return taskMapper.selectList(qw);
    }

    @Override
    public void logicDeleteEntityById(long refId) {
        QueryWrapper<Task> qw = new QueryWrapper<>();
        qw.lambda().eq(Task::getDeleted, 0)
                .eq(Task::getRefId, refId);
        taskMapper.selectList(qw).stream()
                .map(Task::getId)
                .forEach(taskMapper::logicDeleteEntityById);
    }

    @Override
    public TaskRes getLarkTask(Task task) {
        GetTaskReq req = GetTaskReq.newBuilder()
                .taskGuid(task.getTaskGuid())
                .build();
        GetTaskResp resp;
        try {
            resp = client.task().v2().task().get(req);
        } catch (Exception e) {
            log.error("获取飞书任务失败：{}", e.getMessage());
            throw new InternalErrorException(e);
        }
        if (!resp.success()) {
            // 错误码1470404代表任务不存在或已删除
            // Ref: https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/task-v2/task/get
            if (resp.getCode() == 1470404) {
                throw new LarkTaskNotFoundException("任务不存在");
            }
            throw new InternalErrorException();
        }

        // 业务数据处理
        var taskData = resp.getData().getTask();
        task.setCompletedAt(taskData.getCompletedAt());
        taskMapper.updateById(task);

        TaskRes res = new TaskRes();
        if (!Objects.equals(task.getCompletedAt(), "0")) {
            task.setStatus(TaskStatusType.done);
            taskMapper.updateById(task);
            res.setStatus(TaskStatusType.done);
        } else {
            res.setStatus(task.getStatus());
        }
        InstanceRole instanceRole = instanceRoleManager.findEntityById(task.getInstanceRoleId());
        res.setInstanceRoleName(instanceRole.getName());
        res.setId(task.getId());
        res.setTaskUrl(taskData.getUrl());
        res.setSummary(taskData.getSummary());
        res.setCompletedAt(taskData.getCompletedAt());
        res.setType(task.getType());
        return res;
    }

    @Override
    public TaskTemplate getTaskTemplateById(long id) {
        QueryWrapper<TaskTemplate> qw = new QueryWrapper<>();
        qw.lambda().eq(TaskTemplate::getDeleted, 0)
                .eq(TaskTemplate::getId, id);
        return taskTemplateMapper.selectOne(qw);
    }

    @Override
    public List<Task> listTasks(long prevTaskId, int limit) {
        QueryWrapper<Task> qw = new QueryWrapper<>();
        qw.lambda().eq(Task::getDeleted, 0)
                .eq(Task::getStatus, TaskStatusType.todo)
                .gt(Task::getId, prevTaskId)
                .orderByAsc(Task::getId)
                .last("LIMIT " + limit);
        return taskMapper.selectList(qw);
    }

    private String translateType(String type) {

        Map<String, String> dictionary = new HashMap<>();
        dictionary.put("FRAMEWORK_AGREEMENT", "框架协议");
        dictionary.put("PROJECT", "项目");
        dictionary.put("PROJECT_PROPOSAL_PREPARATION", "项目建议书拟定");
        dictionary.put("FRAMEWORK_AGREEMENT_PREPARATION", "框架协议拟定");
        dictionary.put("INVESTMENT_AGREEMENT_PREPARATION", "投资协议拟定");

        return dictionary.get(type);
    }

    @Override
    public TaskListInfo getTaskList(TaskTemplateRefType refType, String name, Long id) {
        TaskListInfo taskListInfo = new TaskListInfo();
        String translateType = translateType(refType.toString());
        //查找任务清单
        String pageToken;
        Optional<Tasklist> tasklist;
        do {
            ListTasklistReq listTasklistReq = ListTasklistReq.newBuilder()
                    .pageSize(50)
                    .userIdType("open_id")
                    .build();
            ListTasklistResp listTasklistResp;
            try {
                listTasklistResp = client.task().v2().tasklist().list(listTasklistReq);
                if (!listTasklistResp.success()) {
                    throw new InternalErrorException();
                }
            } catch (Exception e) {
                log.error("获取任务清单失败", e);
                throw new InternalErrorException(e);
            }
            pageToken = listTasklistResp.getData().getPageToken();
            //查找符合条件的任务清单
            tasklist = Arrays.stream(listTasklistResp.getData().getItems())
                    .filter(item -> item.getCreator().getType().equals("app"))
                    .filter(item -> item.getName().equals(prefix + translateType + "-" + name + "(" + id + ")"))
                    .findFirst();
        } while ((pageToken != null && !pageToken.isEmpty()) && tasklist.isEmpty());
        if (tasklist.isEmpty()) {
            //创建任务清单
            CreateTasklistReq createTasklistReq = CreateTasklistReq.newBuilder()
                    .userIdType("open_id")
                    .inputTasklist(InputTasklist.newBuilder()
                            .name(prefix + translateType + "-" + name + "(" + id + ")")
                            .build())
                    .build();
            CreateTasklistResp createTasklistResp;
            try {
                createTasklistResp = client.task().v2().tasklist().create(createTasklistReq);
                if (!createTasklistResp.success()) {
                    log.error("创建任务清单失败：{}", createTasklistResp.getMsg());
                    throw new InternalErrorException();
                }
            } catch (Exception e) {
                log.error("创建任务清单失败：{}", e.getMessage());
                throw new InternalErrorException(e);
            }
            taskListInfo.setTaskListGuid(createTasklistResp.getData().getTasklist().getGuid());
            taskListInfo.setMembers(Arrays.stream(createTasklistResp.getData().getTasklist().getMembers())
                    .map(Member::getId)
                    .toList());
            return taskListInfo;
        }
        taskListInfo.setTaskListGuid(tasklist.get().getGuid());
        taskListInfo.setMembers(Arrays.stream(tasklist.get().getMembers())
                .map(Member::getId)
                .toList());
        return taskListInfo;
    }

    @Override
    public String getSections(TaskType taskType, String taskListGuid) {
        String type = translateType(taskType.toString());
        //查找任务清单对应的分组
        ListSectionReq listSectionReq = ListSectionReq.newBuilder()
                .resourceType(resourceType)
                .resourceId(taskListGuid)
                .build();
        ListSectionResp listSectionResp;
        try {
            listSectionResp = client.task().v2().section().list(listSectionReq);
            if (!listSectionResp.success()) {
                log.error("获取任务清单分组失败：{}", listSectionResp.getMsg());
                throw new InternalErrorException();
            }
        } catch (Exception e) {
            log.error("获取任务清单分组失败：{}", e.getMessage());
            throw new InternalErrorException(e);
        }
        //查找符合条件的分组
        Optional<SectionSummary> section = Arrays.stream(listSectionResp.getData().getItems())
                .filter(item -> item.getName().equals(type))
                .findFirst();
        if (section.isEmpty()) {
            //创建分组
            CreateSectionReq createSectionReq = CreateSectionReq.newBuilder()
                    .inputSection(InputSection.newBuilder()
                            .name(type)
                            .resourceType(resourceType)
                            .resourceId(taskListGuid)
                            .build())
                    .build();
            CreateSectionResp createSectionResp;
            try {
                createSectionResp = client.task().v2().section().create(createSectionReq);
                if (!createSectionResp.success()) {
                    log.error("创建任务清单分组失败：{}", createSectionResp.getMsg());
                    throw new InternalErrorException();
                }
            } catch (Exception e) {
                log.error("创建任务清单分组失败：{}", e.getMessage());
                throw new InternalErrorException(e);
            }
            return createSectionResp.getData().getSection().getGuid();
        }
        return section.get().getGuid();
    }

    @Override
    public void addTaskToTaskList(TaskListReq req) {
        //任务加入清单
        AddTasklistTaskReq addTasklistTaskReq = AddTasklistTaskReq.newBuilder()
                .taskGuid(req.getTaskGuid())
                .addTasklistTaskReqBody(AddTasklistTaskReqBody.newBuilder()
                        .tasklistGuid(req.getTaskListGuid())
                        .sectionGuid(req.getSectionGuid())
                        .build())
                .build();
        try {
            AddTasklistTaskResp resp = client.task().v2().task().addTasklist(addTasklistTaskReq);
            if (!resp.success()) {
                log.error("任务加入清单失败：{}", resp.getMsg());
                throw new InternalErrorException();
            }
        } catch (Exception e) {
            log.error("任务加入清单失败：{}", e.getMessage());
            throw new InternalErrorException(e);
        }
    }

    @Override
    public void addMemberToTaskList(String taskListGuid, List<String> memberIds) {
        Member[] members = memberIds.stream()
                .map(memberId -> Member.newBuilder()
                        .id(memberId)
                        .build())
                .toArray(Member[]::new);
        AddMembersTasklistReq addMembersTasklistReq = AddMembersTasklistReq.newBuilder()
                .tasklistGuid(taskListGuid)
                .addMembersTasklistReqBody(AddMembersTasklistReqBody.newBuilder()
                        .members(members)
                        .build())
                .build();
        try {
            AddMembersTasklistResp resp = client.task().v2().tasklist().addMembers(addMembersTasklistReq);
            if (!resp.success()) {
                log.error("添加成员到任务清单失败：{}", resp.getMsg());
                throw new InternalErrorException();
            }
        } catch (Exception e) {
            log.error("添加成员到任务清单失败：{}", e.getMessage());
            throw new InternalErrorException(e);
        }
    }

    @Override
    public void removeMemberFromTaskList(String taskListGuid, List<String> memberIds) {
        Member[] members = memberIds.stream()
                .map(memberId -> Member.newBuilder()
                        .id(memberId)
                        .build())
                .toArray(Member[]::new);
        RemoveMembersTasklistReq req = RemoveMembersTasklistReq.newBuilder()
                .tasklistGuid(taskListGuid)
                .userIdType("open_id")
                .removeMembersTasklistReqBody(RemoveMembersTasklistReqBody.newBuilder()
                        .members(members)
                        .build())
                .build();

        // 发起请求
        RemoveMembersTasklistResp resp;
        try {
            resp = client.task().v2().tasklist().removeMembers(req);
            if (!resp.success()) {
                log.error("成员移出失败：{}", resp.getMsg());
                throw new InternalErrorException("成员移出失败");
            }
        } catch (Exception e) {
            log.error("成员移出出错：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateById(Task task) {
        taskMapper.updateById(task);
    }

    @Override
    public Task findEntityById(@NonNull Long id) {
        return taskMapper.findEntityById(id);
    }

}
