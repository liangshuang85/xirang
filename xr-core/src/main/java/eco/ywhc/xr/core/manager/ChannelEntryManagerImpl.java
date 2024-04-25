package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.FileOwnerType;
import eco.ywhc.xr.common.converter.ChannelEntryConverter;
import eco.ywhc.xr.common.model.dto.req.ChannelEntryReq;
import eco.ywhc.xr.common.model.dto.res.AttachmentResponse;
import eco.ywhc.xr.common.model.dto.res.ChannelEntryRes;
import eco.ywhc.xr.common.model.entity.ChannelEntry;
import eco.ywhc.xr.core.mapper.ChannelEntryMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InvalidInputException;

import java.util.List;
import java.util.Set;

/**
 * 渠道录入信息(eco.ywhc.xr.common.model.entity.BChannelEntry)表服务实现类
 *
 * @author makejava
 * @since 2024-04-18 11:51:08
 */
@Service
@RequiredArgsConstructor
public class ChannelEntryManagerImpl implements ChannelEntryManager {

    private final ChannelEntryMapper channelEntryMapper;

    private final ChannelEntryConverter channelEntryConverter;

    private final AttachmentManager attachmentManager;

    @Override
    public long createOne(@NonNull ChannelEntryReq req, long clueId) {
        ChannelEntry channelEntry = channelEntryConverter.fromRequest(req);
        channelEntry.setClueId(clueId);
        channelEntryMapper.insert(channelEntry);
        long id = channelEntry.getId();
        linkAttachments(req, id);
        return id;
    }

    @Override
    public ChannelEntry findEntityByClueId(long clueId) {
        QueryWrapper<ChannelEntry> qw = new QueryWrapper<>();
        qw.lambda().eq(ChannelEntry::getDeleted, false)
                .eq(ChannelEntry::getClueId, clueId);
        return channelEntryMapper.selectOne(qw);
    }

    @Override
    public ChannelEntryRes findByClueId(long clueId) {
        ChannelEntryRes channelEntry = channelEntryConverter.toResponse(findEntityByClueId(clueId));
        List<AttachmentResponse> projectInfoAttachments = attachmentManager.findManyByOwnerId(channelEntry.getId(), FileOwnerType.PROJECT_INFO_STATISTICS);
        channelEntry.setProjectInfoAttachments(projectInfoAttachments);
        List<AttachmentResponse> projectFundingAttachments = attachmentManager.findManyByOwnerId(channelEntry.getId(), FileOwnerType.PROJECT_FUNDING);
        channelEntry.setProjectFundingAttachments(projectFundingAttachments);
        return channelEntry;
    }

    @Override
    public void logicDeleteEntityByClueId(long clueId) {
        ChannelEntry entity = findEntityByClueId(clueId);
        channelEntryMapper.logicDeleteEntityById(entity.getId());
    }

    private void linkAttachments(ChannelEntryReq req, long id) {
        Set<Long> projectFundingAttachmentIds = req.getProjectFundingAttachmentIds();
        if (CollectionUtils.isNotEmpty(projectFundingAttachmentIds)) {
            if (!attachmentManager.containsAll(projectFundingAttachmentIds)) {
                throw new InvalidInputException("项目收资表的附件ID列表错误");
            }
            attachmentManager.update(projectFundingAttachmentIds, FileOwnerType.PROJECT_FUNDING, id);
        }
        Set<Long> projectInfoAttachmentIds = req.getProjectInfoAttachmentIds();
        if (CollectionUtils.isNotEmpty(projectInfoAttachmentIds)) {
            if (!attachmentManager.containsAll(projectInfoAttachmentIds)) {
                throw new InvalidInputException("项目基本情况统计表的附件ID列表错误");
            }
            attachmentManager.update(projectFundingAttachmentIds, FileOwnerType.PROJECT_INFO_STATISTICS, id);
        }
    }

}
