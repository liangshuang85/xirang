package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementReq;
import eco.ywhc.xr.common.model.dto.res.FrameworkAgreementRes;
import eco.ywhc.xr.common.model.entity.FrameworkAgreement;
import eco.ywhc.xr.common.model.query.FrameworkAgreementQuery;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.manager.BaseManager;

/**
 * 框架协议项目
 */
@Transactional(rollbackFor = {Exception.class})
public interface FrameworkAgreementManager extends BaseManager<Long, FrameworkAgreement, FrameworkAgreementReq, FrameworkAgreementRes, FrameworkAgreementQuery> {

    @Override
    FrameworkAgreement findEntityById(@NonNull Long id);

    /**
     * 比较并更新关联附件信息
     *
     * @param req 请求参数
     * @param id  关联ID
     */
    void compareAndUpdateAttachments(FrameworkAgreementReq req, long id);

    /**
     * 比较然后更新项目建议书批复状态和框架协议书签署状态
     */
    void compareAndUpdateStatus(FrameworkAgreement frameworkAgreement);

    /**
     * 根据关联线索ID查询是否存在框架协议
     *
     * @param clueId 关联线索ID
     */
    boolean isExistByClueId(long clueId);

}
