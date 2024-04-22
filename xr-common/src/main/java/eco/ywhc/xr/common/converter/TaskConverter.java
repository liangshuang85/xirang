package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.common.model.entity.TaskTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TaskConverter {

    Task fromTaskTemplate(TaskTemplate taskTemplate);

    @Mapping(target = "id", ignore = true)
    Task with(Task oldTask);

}
