package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.res.AssigneeRes;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = {Exception.class})
public interface ProfileService {

    AssigneeRes getProfile();

}
