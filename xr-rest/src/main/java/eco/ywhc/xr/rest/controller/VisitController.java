package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.common.model.dto.req.VisitReq;
import eco.ywhc.xr.core.manager.VisitManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.sugar.crud.model.CreateResult;
import org.sugar.crud.model.OperationResult;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VisitController {

    private final VisitManager visitManager;

    /**
     * 添加访问记录
     */
    @PostMapping("/visits")
    public CreateResult<Long> createOne(@Valid @RequestBody VisitReq req) {
        Long affected = visitManager.createOne(req);
        return CreateResult.of(affected);
    }

    /**
     * 删除访问记录
     *
     * @param id 访问记录id
     */
    @DeleteMapping("/visits/{id}")
    public OperationResult logicDeleteOne(@PathVariable Long id) {
        int affected = visitManager.logicDeleteOne(id);
        return OperationResult.of(affected);
    }

}
