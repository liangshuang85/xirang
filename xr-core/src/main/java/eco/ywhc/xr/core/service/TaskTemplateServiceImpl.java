package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.converter.TaskTemplateConverter;
import eco.ywhc.xr.common.model.dto.req.TaskTemplateReq;
import eco.ywhc.xr.common.model.dto.res.TaskTemplateRes;
import eco.ywhc.xr.common.model.entity.TaskTemplate;
import eco.ywhc.xr.core.manager.TaskTemplateManager;
import eco.ywhc.xr.core.mapper.TaskTemplateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskTemplateServiceImpl implements TaskTemplateService {

    private final TaskTemplateConverter taskTemplateConverter;

    private final TaskTemplateManager taskTemplateManager;

    private final TaskTemplateMapper taskTemplateMapper;

    @Override
    public Long createOne(TaskTemplateReq req) {
        TaskTemplate taskTemplate = taskTemplateConverter.fromRequest(req);
        taskTemplateMapper.insert(taskTemplate);
        return taskTemplate.getId();
    }

    @Override
    public PageableModelSet<TaskTemplateRes> findMany() {
        QueryWrapper<TaskTemplate> qw = new QueryWrapper<>();
        qw.lambda().eq(TaskTemplate::getDeleted, 0);
        List<TaskTemplate> rows = taskTemplateMapper.selectList(qw);
        List<TaskTemplateRes> results = rows.stream().map(taskTemplateConverter::toResponse).toList();
        return PageableModelSet.of(results);
    }

    @Override
    public TaskTemplateRes findOne(long id) {
        TaskTemplate taskTemplate = taskTemplateManager.mustFindEntityById(id);
        return taskTemplateConverter.toResponse(taskTemplate);
    }

    @Override
    public int updateOne(long id, TaskTemplateReq req) {
        TaskTemplate taskTemplate = taskTemplateManager.mustFindEntityById(id);
        taskTemplateConverter.update(req, taskTemplate);
        return taskTemplateMapper.updateById(taskTemplate);
    }

    @Override
    public int deleteOne(long id) {
        taskTemplateManager.mustFindEntityById(id);
        return taskTemplateMapper.logicDeleteEntityById(id);
    }

}
