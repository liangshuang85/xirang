package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.FileOwnerType;
import eco.ywhc.xr.common.converter.FundingConverter;
import eco.ywhc.xr.common.model.dto.req.FundingReq;
import eco.ywhc.xr.common.model.dto.res.AttachmentResponse;
import eco.ywhc.xr.common.model.dto.res.FundingRes;
import eco.ywhc.xr.common.model.entity.Funding;
import eco.ywhc.xr.core.mapper.FundingMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InvalidInputException;

import java.util.List;
import java.util.Set;

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

    private final AttachmentManager attachmentManager;

    @Override
    public long createOne(@NonNull FundingReq req, long clueId) {
        Funding funding = fundingConverter.fromRequest(req);
        funding.setClueId(clueId);
        fundingMapper.insert(funding);
        long id = funding.getId();
        linkAttachments(req, id);
        return id;
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
        FundingRes funding = fundingConverter.toResponse(findEntityByClueId(clueId));
        List<AttachmentResponse> attachments = attachmentManager.findManyByOwnerId(funding.getId(), FileOwnerType.MAJOR_ELECTRICITY_CONSUMERS);
        funding.setMajorElectricityConsumerAttachments(attachments);
        return funding;
    }

    @Override
    public void logicDeleteEntityByClueId(long clueId) {
        Funding entity = findEntityByClueId(clueId);
        fundingMapper.logicDeleteEntityById(entity.getId());
    }

    private void linkAttachments(FundingReq req, long id) {
        Set<Long> majorElectricityConsumerIds = req.getMajorElectricityConsumerAttachmentIds();
        if (CollectionUtils.isNotEmpty(majorElectricityConsumerIds)) {
            if (!attachmentManager.containsAll(majorElectricityConsumerIds)) {
                throw new InvalidInputException("附件ID列表错误");
            }
            attachmentManager.update(majorElectricityConsumerIds, FileOwnerType.MAJOR_ELECTRICITY_CONSUMERS, id);
        }
    }

}
