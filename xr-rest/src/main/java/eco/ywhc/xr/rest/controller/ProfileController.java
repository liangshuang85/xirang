package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.common.model.dto.res.AssigneeRes;
import eco.ywhc.xr.core.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 个人信息接口
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 获取当前登录用户的个人信息
     */
    @GetMapping("/profile")
    public AssigneeRes getProfile() {
        return profileService.getProfile();
    }

}
