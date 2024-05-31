package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.impexp.InstanceRoleDump;
import eco.ywhc.xr.common.model.dto.req.InstanceRoleReq;
import eco.ywhc.xr.common.model.dto.res.InstanceRoleRes;
import eco.ywhc.xr.common.model.entity.InstanceRole;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 实例角色
 */
@Mapper
public interface InstanceRoleConverter extends SimpleConverter<InstanceRole, InstanceRoleReq, InstanceRoleRes> {

    InstanceRoleDump toDump(InstanceRole source);

    List<InstanceRoleDump> toDump(Collection<InstanceRole> source);

    InstanceRole fromDump(InstanceRoleDump source);

    List<InstanceRole> fromDump(Collection<InstanceRoleDump> source);

}
