package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.ApplicationReq;
import eco.ywhc.xr.common.model.dto.res.ApplicationRes;
import eco.ywhc.xr.common.model.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 应用
 */
@Mapper
public interface ApplicationConverter extends SimpleConverter<Application, ApplicationReq, ApplicationRes> {

    @Mapping(target = "keyPairGenerated", expression = "java(org.apache.commons.lang3.StringUtils.isNotBlank(source.getPublicKey()))")
    ApplicationRes toResponse(Application source);

}
