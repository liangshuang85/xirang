package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.PermissionResourceReq;
import eco.ywhc.xr.common.model.dto.res.PermissionResourceRes;
import eco.ywhc.xr.common.model.entity.PermissionResource;
import org.mapstruct.Mapper;

@Mapper
public interface PermissionResourceConverter extends SimpleConverter<PermissionResource, PermissionResourceReq, PermissionResourceRes> {

}
