package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.common.model.dto.res.InstanceRoleRes;
import eco.ywhc.xr.common.model.query.InstanceRoleQuery;
import eco.ywhc.xr.core.service.InstanceRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sugar.crud.model.PageableModelSet;

/**
 * 实例角色接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class InstanceRoleController {

    private final InstanceRoleService instanceRoleService;

    /**
     * 获取实例角色列表，结果以分页或不分页的形式返回
     */
    @GetMapping("/instanceRoles")
    public PageableModelSet<InstanceRoleRes> findAllEntities(@ParameterObject InstanceRoleQuery query) {
        return instanceRoleService.findMany(query);
    }

}
