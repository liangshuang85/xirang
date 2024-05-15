package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.RoleReq;
import eco.ywhc.xr.common.model.dto.res.RoleRes;
import eco.ywhc.xr.common.model.entity.Role;
import org.mapstruct.Mapper;

/**
 * 角色
 */
@Mapper
public interface RoleConverter extends SimpleConverter<Role, RoleReq, RoleRes> {

}
