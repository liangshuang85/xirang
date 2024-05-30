package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.res.TaskRes;
import eco.ywhc.xr.common.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TaskConverter {

    @Mapping(source = "url", target = "taskUrl")
    TaskRes toResponse(Task task);

}
