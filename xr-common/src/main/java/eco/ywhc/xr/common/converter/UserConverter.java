package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.entity.User;
import eco.ywhc.xr.common.security.CurrentUser;
import org.mapstruct.Mapper;

@Mapper
public interface UserConverter {

    CurrentUser toCurrentUser(User user);

}
