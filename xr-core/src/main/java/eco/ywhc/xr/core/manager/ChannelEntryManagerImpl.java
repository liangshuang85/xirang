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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChannelEntryManagerImpl implements ChannelEntryManager {

    private final AttachmentManager attachmentManager;

    private final ChannelEntryConverter channelEntryConverter;

    private final ChannelEntryMapper channelEntryMapper;

    @Override
    public void createChannelEntry(ChannelEntryReq req, long refId) {
        ChannelEntry channelEntry = channelEntryConverter.fromRequest(req);
        channelEntry.setRefId(refId);
        channelEntryMapper.insert(channelEntry);
        // 比较并更新附件信息
        compareAndUpdateAttachments(req, channelEntry.getId());
    }

    @Override
    public void updateChannelEntry(ChannelEntryReq req, long refId) {
        ChannelEntry channelEntry = getCurrentChannelEntry(refId);
        channelEntryConverter.update(req, channelEntry);
        channelEntry.setRefId(refId);
        // 比较并更新附件
        compareAndUpdateAttachments(req, channelEntry.getId());
        channelEntryMapper.updateById(channelEntry);
    }

    @Override
    public void logicDeleteChannelEntry(long refId) {
        ChannelEntry channelEntry = getCurrentChannelEntry(refId);
        attachmentManager.deleteByOwnerId(channelEntry.getId());
        channelEntryMapper.logicDeleteEntityById(channelEntry.getId());
    }

    @Override
    public ChannelEntryRes getChannelEntryByRefId(long refId) {
        ChannelEntryRes channelEntry = channelEntryConverter.toResponse(getCurrentChannelEntry(refId));
        List<AttachmentResponse> projectInfoAttachments = attachmentManager.findManyByOwnerId(channelEntry.getId(), FileOwnerType.PROJECT_INFO_STATISTICS);
        List<AttachmentResponse> projectFundingAttachments = attachmentManager.findManyByOwnerId(channelEntry.getId(), FileOwnerType.PROJECT_FUNDING);
        channelEntry.setProjectInfoAttachments(projectInfoAttachments);
        channelEntry.setProjectFundingAttachments(projectFundingAttachments);
        return channelEntry;
    }

    private ChannelEntry getCurrentChannelEntry(long refId) {
        QueryWrapper<ChannelEntry> qw = new QueryWrapper<>();
        qw.lambda().eq(ChannelEntry::getDeleted, false)
                .eq(ChannelEntry::getRefId, refId);
        return channelEntryMapper.selectOne(qw);
    }

    private void compareAndUpdateAttachments(ChannelEntryReq req, long id) {
        Set<Long> projectFundingAttachmentIds = req.getProjectFundingAttachmentIds();
        attachmentManager.compareAndUpdate(id, projectFundingAttachmentIds, FileOwnerType.PROJECT_FUNDING);
        Set<Long> projectInfoAttachmentIds = req.getProjectInfoAttachmentIds();
        attachmentManager.compareAndUpdate(id, projectInfoAttachmentIds, FileOwnerType.PROJECT_INFO_STATISTICS);
    }

}
