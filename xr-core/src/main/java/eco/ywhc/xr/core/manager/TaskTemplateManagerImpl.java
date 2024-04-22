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

import java.util.List;

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

}
