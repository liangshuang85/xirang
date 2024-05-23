package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import eco.ywhc.xr.common.converter.ApprovalTemplateConverter;
import eco.ywhc.xr.common.model.dto.req.ApprovalTemplateReq;
import eco.ywhc.xr.common.model.dto.res.ApprovalTemplateRes;
import eco.ywhc.xr.common.model.entity.ApprovalTemplate;
import eco.ywhc.xr.core.manager.ApprovalTemplateManager;
import eco.ywhc.xr.core.mapper.ApprovalTemplateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.sugar.crud.model.PageableModelSet;
import org.sugar.crud.query.BasePageQuery;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApprovalTemplateServiceImpl implements ApprovalTemplateService {

    private final ApprovalTemplateConverter approvalTemplateConverter;

    private final ApprovalTemplateManager approvalTemplateManager;

    private final ApprovalTemplateMapper approvalTemplateMapper;

    @Override
    public Long createOne(ApprovalTemplateReq req) {
        ApprovalTemplate approvalTemplate = approvalTemplateConverter.fromRequest(req);
        approvalTemplateMapper.insert(approvalTemplate);
        return approvalTemplate.getId();
    }

    @Override
    public PageableModelSet<ApprovalTemplateRes> findMany() {
        QueryWrapper<ApprovalTemplate> qw = new QueryWrapper<>();
        qw.lambda().eq(ApprovalTemplate::getDeleted, 0);
        BasePageQuery query = new BasePageQuery();
        IPage<ApprovalTemplate> rows = approvalTemplateMapper.selectPage(query.paging(true), qw);
        if (CollectionUtils.isEmpty(rows.getRecords())) {
            return PageableModelSet.from(query.paging());
        }
        var results = rows.convert(approvalTemplateConverter::toResponse);
        return PageableModelSet.from(results);
    }

    @Override
    public ApprovalTemplateRes findOne(long id) {
        ApprovalTemplate approvalTemplate = approvalTemplateManager.mustFindEntityById(id);
        return approvalTemplateConverter.toResponse(approvalTemplate);
    }

    @Override
    public int updateOne(long id, ApprovalTemplateReq req) {
        ApprovalTemplate approvalTemplate = approvalTemplateManager.mustFindEntityById(id);
        approvalTemplateConverter.update(req, approvalTemplate);
        return approvalTemplateMapper.updateById(approvalTemplate);
    }

    @Override
    public int deleteOne(long id) {
        approvalTemplateManager.mustFindEntityById(id);
        return approvalTemplateMapper.logicDeleteEntityById(id);
    }

}
