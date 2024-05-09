package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.common.model.entity.InstanceRole;
import eco.ywhc.xr.core.manager.InstanceRoleLarkMemberManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 实例角色接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class InstanceRoleController {

    private final InstanceRoleLarkMemberManager instanceRoleLarkMemberManager;

    /**
     * 查询所有启用的实例角色
     */
    @GetMapping("/instanceRoles")
    public List<InstanceRole> findAllInstanceRoles() {
        return instanceRoleLarkMemberManager.findAllInstanceRoles();
    }

}
