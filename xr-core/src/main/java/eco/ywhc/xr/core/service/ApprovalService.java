package eco.ywhc.xr.core.service;

public interface ApprovalService {

    /**
     * 发起一个飞书审批
     *
     * @param id 审批ID
     */
    int startLarkApproval(long id);

}
