package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.entity.Permission;
import org.mapstruct.Mapper;

@Mapper
public interface PermissionConverter {

    PermissionRes toResponse(Permission source);

}
