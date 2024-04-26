package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.core.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sugar.crud.model.OperationResult;

/**
 * 审批接口
 */
@RestController
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    /**
     * 发起审批
     *
     * @param id 审批ID
     */
    @PostMapping("/approvals/{id}:start")
    public OperationResult startLarkApproval(@PathVariable long id) {
        int affected = approvalService.startLarkApproval(id);
        return OperationResult.of(affected);
    }

}