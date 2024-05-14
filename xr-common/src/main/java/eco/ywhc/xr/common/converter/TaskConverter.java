package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.res.TaskRes;
import eco.ywhc.xr.common.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TaskConverter {

    TaskRes toResponse(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Task with(Task oldTask);

}
