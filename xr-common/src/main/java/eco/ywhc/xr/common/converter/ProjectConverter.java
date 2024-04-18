package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.ProjectReq;
import eco.ywhc.xr.common.model.dto.res.ProjectRes;
import eco.ywhc.xr.common.model.entity.Project;
import org.mapstruct.Mapper;

@Mapper
public interface ProjectConverter extends SimpleConverter<Project, ProjectReq, ProjectRes> {

}
