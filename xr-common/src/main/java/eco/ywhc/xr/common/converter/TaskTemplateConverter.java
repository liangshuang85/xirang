package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.TaskTemplateReq;
import eco.ywhc.xr.common.model.dto.res.TaskTemplateRes;
import eco.ywhc.xr.common.model.entity.TaskTemplate;
import org.mapstruct.Mapper;

@Mapper
public interface TaskTemplateConverter extends SimpleConverter<TaskTemplate, TaskTemplateReq, TaskTemplateRes> {

}
