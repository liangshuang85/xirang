package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementReq;
import eco.ywhc.xr.common.model.dto.res.FrameworkAgreementRes;
import eco.ywhc.xr.common.model.entity.FrameworkAgreement;
import eco.ywhc.xr.common.model.query.FrameworkAgreementQuery;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.model.PageableModelSet;
import org.sugar.crud.service.BaseService;

@Transactional(rollbackFor = {Exception.class})
public interface FrameworkAgreementService
        extends BaseService<Long, FrameworkAgreement, FrameworkAgreementReq, FrameworkAgreementRes, FrameworkAgreementQuery> {

    /**
     * 新建框架协议项目
     *
     * @param req 框架协议项目req
     */
    @Override
    Long createOne(@NonNull FrameworkAgreementReq req);

    /**
     * 获取框架协议项目列表，结果以分页形式返回
     *
     * @param query 框架协议项目查询条件
     */
    @Override
    PageableModelSet<FrameworkAgreementRes> findMany(@NonNull FrameworkAgreementQuery query);

    /**
     * 查找指定框架协议项目
     *
     * @param id 框架协议项目ID
     */
    @Override
    FrameworkAgreementRes findOne(@NonNull Long id);

    /**
     * 更新指定框架协议项目
     *
     * @param id  框架协议项目ID
     * @param req 更新req
     */
    @Override
    int updateOne(@NonNull final Long id, @NonNull FrameworkAgreementReq req);

    /**
     * 逻辑删除指定框架协议项目
     *
     * @param id 框架协议项目ID
     */
    int logicDeleteOne(@NonNull final Long id);

}
