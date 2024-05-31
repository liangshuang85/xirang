package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.impexp.RoleDump;
import eco.ywhc.xr.common.model.dto.req.RoleReq;
import eco.ywhc.xr.common.model.dto.res.RoleRes;
import eco.ywhc.xr.common.model.entity.Role;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 角色
 */
@Mapper
public interface RoleConverter extends SimpleConverter<Role, RoleReq, RoleRes> {

    RoleDump toDump(Role source);

    List<RoleDump> toDump(Collection<Role> source);

    Role fromDump(RoleDump source);

    List<Role> fromDump(Collection<RoleDump> source);

}
