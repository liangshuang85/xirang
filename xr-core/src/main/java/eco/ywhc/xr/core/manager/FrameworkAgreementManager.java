package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementReq;
import eco.ywhc.xr.common.model.dto.res.FrameworkAgreementChannelEntryRes;
import eco.ywhc.xr.common.model.dto.res.FrameworkAgreementRes;
import eco.ywhc.xr.common.model.entity.FrameworkAgreement;
import eco.ywhc.xr.common.model.entity.FrameworkAgreementChannelEntry;
import eco.ywhc.xr.common.model.query.FrameworkAgreementQuery;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.manager.BaseManager;

@Transactional(rollbackFor = {Exception.class})
public interface FrameworkAgreementManager extends BaseManager<Long, FrameworkAgreement, FrameworkAgreementReq, FrameworkAgreementRes, FrameworkAgreementQuery> {

    @Override
    FrameworkAgreement findEntityById(@NonNull Long id);

    /**
     * 根据框架协议ID查询渠道录入信息
     *
     * @param id 框架协议ID
     */
    FrameworkAgreementChannelEntry getFrameworkAgreementChannelEntryById(long id);

    /**
     * 根据框架协议ID查询渠道录入信息
     *
     * @param id 框架协议ID
     */
    FrameworkAgreementChannelEntryRes getChannelEntryByFrameworkAgreementId(long id);

    /**
     * 比较并更新关联附件信息
     *
     * @param req 请求参数
     * @param id  关联ID
     */
    void compareAndUpdateAttachments(Object req, long id);

}
