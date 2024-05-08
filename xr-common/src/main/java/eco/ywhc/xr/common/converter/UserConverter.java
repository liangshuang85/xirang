package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.RequestContextUser;
import eco.ywhc.xr.common.model.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserConverter {

    RequestContextUser toRequestContextUser(User user);

}
