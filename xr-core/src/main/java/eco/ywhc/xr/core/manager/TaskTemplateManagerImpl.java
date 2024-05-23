package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.TaskTemplateRefType;
import eco.ywhc.xr.common.constant.TaskType;
import eco.ywhc.xr.common.model.entity.TaskTemplate;
import eco.ywhc.xr.core.mapper.TaskTemplateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskTemplateManagerImpl implements TaskTemplateManager {

    private final TaskTemplateMapper taskTemplateMapper;

    @Override
    public List<TaskTemplate> listByType(@NonNull TaskTemplateRefType refType, @NonNull TaskType type) {
        QueryWrapper<TaskTemplate> qw = new QueryWrapper<>();
        qw.lambda().eq(TaskTemplate::getDeleted, 0)
                .eq(TaskTemplate::getRefType, refType)
                .eq(TaskTemplate::getType, type);
        return taskTemplateMapper.selectList(qw);
    }

    @Override
    public TaskTemplate findEntityById(long id) {
        return taskTemplateMapper.findEntityById(id);
    }

    @Override
    public TaskTemplate mustFindEntityById(long id) {
        TaskTemplate taskTemplate = findEntityById(id);
        return Optional.ofNullable(taskTemplate).orElseThrow(ResourceNotFoundException::new);
    }

}
