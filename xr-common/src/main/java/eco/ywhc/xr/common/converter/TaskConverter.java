package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.res.TaskRes;
import eco.ywhc.xr.common.model.entity.Task;
import eco.ywhc.xr.common.model.entity.TaskTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TaskConverter {

    @Mapping(target = "id", ignore = true)
    Task fromTaskTemplate(TaskTemplate taskTemplate);

    TaskRes toResponse(Task task);

    @Mapping(target = "id", ignore = true)
    Task with(Task oldTask);

}
