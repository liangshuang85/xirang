package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.ProjectInformationReq;
import eco.ywhc.xr.common.model.dto.res.ProjectInformationRes;
import eco.ywhc.xr.common.model.entity.ProjectInformation;
import org.mapstruct.Mapper;

@Mapper
public interface ProjectInformationConverter extends SimpleConverter<ProjectInformation, ProjectInformationReq, ProjectInformationRes> {

}
