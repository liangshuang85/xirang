package eco.ywhc.xr.rest.admin;

import eco.ywhc.xr.common.model.dto.req.TaskTemplateReq;
import eco.ywhc.xr.common.model.dto.res.TaskTemplateRes;
import eco.ywhc.xr.core.service.TaskTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.sugar.crud.model.CreateResult;
import org.sugar.crud.model.OperationResult;
import org.sugar.crud.model.PageableModelSet;

/**
 * 任务模板接口
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminTaskTemplateController {

    private final TaskTemplateService taskTemplateService;

    /**
     * 创建一个任务模板
     */
    @PostMapping("/admin/rest/taskTemplates")
    public CreateResult<Long> createOne(@Valid @RequestBody TaskTemplateReq req) {
        Long id = taskTemplateService.createOne(req);
        return CreateResult.of(id);
    }

    /**
     * 获取任务模板列表，结果以分页形式返回
     */
    @GetMapping("/admin/rest/taskTemplates")
    public PageableModelSet<TaskTemplateRes> findMany() {
        return taskTemplateService.findMany();
    }

    /**
     * 查询指定ID任务模板的详情
     *
     * @param id 任务模板ID
     */
    @GetMapping("/admin/rest/taskTemplates/{id}")
    public TaskTemplateRes findOne(@PathVariable long id) {
        return taskTemplateService.findOne(id);
    }

    /**
     * 更新一个任务模板
     *
     * @param id 任务模板ID
     */
    @PutMapping("/admin/rest/taskTemplates/{id}")
    public OperationResult updateOne(@PathVariable long id, @Valid @RequestBody TaskTemplateReq req) {
        int affected = taskTemplateService.updateOne(id, req);
        return OperationResult.of(affected);
    }

    /**
     * 删除一个任务模板
     *
     * @param id 任务模板ID
     */
    @DeleteMapping("/admin/rest/taskTemplates/{id}")
    public OperationResult deleteOne(@PathVariable long id) {
        int affected = taskTemplateService.deleteOne(id);
        return OperationResult.of(affected);
    }

}
