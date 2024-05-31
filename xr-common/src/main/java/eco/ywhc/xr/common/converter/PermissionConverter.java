package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.impexp.PermissionDump;
import eco.ywhc.xr.common.model.dto.req.PermissionReq;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.entity.Permission;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface PermissionConverter extends SimpleConverter<Permission, PermissionReq, PermissionRes> {

    PermissionDump toDump(Permission source);

    List<PermissionDump> toDump(Collection<Permission> source);

    Permission fromDump(PermissionDump source);

    List<Permission> fromDump(Collection<PermissionDump> source);

}
