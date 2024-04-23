package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.converter.FundingConverter;
import eco.ywhc.xr.common.model.dto.req.FundingReq;
import eco.ywhc.xr.common.model.dto.res.FundingRes;
import eco.ywhc.xr.common.model.entity.Funding;
import eco.ywhc.xr.core.mapper.FundingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * 项目收资信息(eco.ywhc.xr.common.model.entity.BFunding)表服务实现类
 *
 * @author makejava
 * @since 2024-04-18 11:51:08
 */
@Service
@RequiredArgsConstructor
public class FundingManagerImpl implements FundingManager {

    private final FundingMapper fundingMapper;

    private final FundingConverter fundingConverter;

    @Override
    public long createOne(@NonNull FundingReq req, long clueId) {
        Funding funding = fundingConverter.fromRequest(req);
        funding.setClueId(clueId);
        fundingMapper.insert(funding);
        return funding.getId();
    }

    @Override
    public Funding findEntityByClueId(long clueId) {
        QueryWrapper<Funding> qw = new QueryWrapper<>();
        qw.lambda().eq(Funding::getDeleted, false)
                .eq(Funding::getClueId, clueId);
        return fundingMapper.selectOne(qw);
    }

    @Override
    public FundingRes findByClueId(long clueId) {
        return fundingConverter.toResponse(findEntityByClueId(clueId));
    }

    @Override
    public void logicDeleteEntityByClueId(long clueId) {
        Funding entity = findEntityByClueId(clueId);
        fundingMapper.logicDeleteEntityById(entity.getId());
    }

}
