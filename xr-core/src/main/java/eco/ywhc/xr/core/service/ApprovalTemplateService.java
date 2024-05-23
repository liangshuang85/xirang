package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.req.ApprovalTemplateReq;
import eco.ywhc.xr.common.model.dto.res.ApprovalTemplateRes;
import org.sugar.crud.model.PageableModelSet;

public interface ApprovalTemplateService {

    /**
     * 创建一个审批模板
     */
    Long createOne(ApprovalTemplateReq req);

    /**
     * 查询审批模板列表
     */
    PageableModelSet<ApprovalTemplateRes> findMany();

    /**
     * 查询一个审批模板
     */
    ApprovalTemplateRes findOne(long id);

    /**
     * 更新一个审批模板
     */
    int updateOne(long id, ApprovalTemplateReq req);

    /**
     * 删除一个审批模板
     */
    int deleteOne(long id);

}
