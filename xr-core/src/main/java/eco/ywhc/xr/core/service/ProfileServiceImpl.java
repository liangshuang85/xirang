package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.res.AssigneeRes;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.common.security.CurrentUser;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import eco.ywhc.xr.core.util.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final LarkEmployeeManager larkEmployeeManager;

    @Override
    public AssigneeRes getProfile() {
        CurrentUser currentUser = SessionUtils.currentUser();
        LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployee(currentUser.getLarkOpenId());
        return AssigneeRes.builder()
                .assigneeId(currentUser.getLarkOpenId())
                .assigneeName(larkEmployee.getName())
                .avatarInfo(larkEmployee.getAvatarInfo())
                .build();
    }

}
