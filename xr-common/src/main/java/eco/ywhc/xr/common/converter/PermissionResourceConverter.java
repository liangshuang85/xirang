package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.impexp.PermissionResourceDump;
import eco.ywhc.xr.common.model.dto.req.PermissionResourceReq;
import eco.ywhc.xr.common.model.dto.res.PermissionResourceRes;
import eco.ywhc.xr.common.model.entity.PermissionResource;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface PermissionResourceConverter extends SimpleConverter<PermissionResource, PermissionResourceReq, PermissionResourceRes> {

    PermissionResourceDump toDump(PermissionResource source);

    List<PermissionResourceDump> toDump(Collection<PermissionResource> source);

    PermissionResource fromDump(PermissionResourceDump source);

    List<PermissionResource> fromDump(Collection<PermissionResourceDump> source);

}
