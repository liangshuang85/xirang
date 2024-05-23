package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.converter.ApprovalTemplateConverter;
import eco.ywhc.xr.common.model.dto.req.ApprovalTemplateReq;
import eco.ywhc.xr.common.model.dto.res.ApprovalTemplateRes;
import eco.ywhc.xr.common.model.entity.ApprovalTemplate;
import eco.ywhc.xr.core.manager.ApprovalTemplateManager;
import eco.ywhc.xr.core.mapper.ApprovalTemplateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;

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
        List<ApprovalTemplate> rows = approvalTemplateMapper.selectList(qw);
        List<ApprovalTemplateRes> results = rows.stream().map(approvalTemplateConverter::toResponse).toList();
        return PageableModelSet.of(results);
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
