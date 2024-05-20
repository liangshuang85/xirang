package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.InstanceRoleReq;
import eco.ywhc.xr.common.model.dto.res.InstanceRoleRes;
import eco.ywhc.xr.common.model.entity.InstanceRole;
import org.mapstruct.Mapper;

/**
 * 实例角色
 */
@Mapper
public interface InstanceRoleConverter extends SimpleConverter<InstanceRole, InstanceRoleReq, InstanceRoleRes> {

}
