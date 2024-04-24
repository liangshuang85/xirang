package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.common.model.dto.res.AssigneeRes;
import eco.ywhc.xr.core.service.AssigneeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sugar.crud.model.PageableModelSet;

/**
 * 负责人接口
 */
@RestController
@RequiredArgsConstructor
public class AssigneeController {

    private final AssigneeService assigneeService;

    /**
     * 获取全部负责人
     */
    @GetMapping("/assignees")
    public PageableModelSet<AssigneeRes> listAssignees() {
        return assigneeService.findAll();
    }

}
