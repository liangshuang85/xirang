package eco.ywhc.xr.rest.admin;

import eco.ywhc.xr.common.model.dto.req.ApprovalTemplateReq;
import eco.ywhc.xr.common.model.dto.res.ApprovalTemplateRes;
import eco.ywhc.xr.core.service.ApprovalTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.sugar.crud.model.OperationResult;
import org.sugar.crud.model.PageableModelSet;

/**
 * 审批模板接口
 **/
@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminApprovalTemplateController {

    private final ApprovalTemplateService approvalTemplateService;

    /**
     * 创建一个审批模板
     */
    @PostMapping("/admin/rest/approvalTemplates")
    public Long createOneApprovalTemplate(@RequestBody @Valid ApprovalTemplateReq req) {
        return approvalTemplateService.createOne(req);
    }

    /**
     * 获取审批模板列表，结果以分页形式返回
     */
    @GetMapping("/admin/rest/approvalTemplates")
    public PageableModelSet<ApprovalTemplateRes> findMany() {
        return approvalTemplateService.findMany();
    }

    /**
     * 查询指定ID审批模板的详情
     *
     * @param id 审批模板ID
     */
    @GetMapping("/admin/rest/approvalTemplates/{id}")
    public ApprovalTemplateRes findOne(@PathVariable long id) {
        return approvalTemplateService.findOne(id);
    }

    /**
     * 更新一个审批模板
     *
     * @param id 审批模板ID
     */
    @PutMapping("/admin/rest/approvalTemplates/{id}")
    public OperationResult updateOne(@PathVariable long id, @RequestBody @Valid ApprovalTemplateReq req) {
        int affected = approvalTemplateService.updateOne(id, req);
        return OperationResult.of(affected);
    }

    /**
     * 删除一个审批模板
     *
     * @param id 审批模板ID
     */
    @DeleteMapping("/admin/rest/approvalTemplates/{id}")
    public OperationResult deleteOne(@PathVariable long id) {
        int affected = approvalTemplateService.deleteOne(id);
        return OperationResult.of(affected);
    }

}
