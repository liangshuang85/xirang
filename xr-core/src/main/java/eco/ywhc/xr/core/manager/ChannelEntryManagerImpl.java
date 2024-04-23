package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.converter.ChannelEntryConverter;
import eco.ywhc.xr.common.model.dto.req.ChannelEntryReq;
import eco.ywhc.xr.common.model.dto.res.ChannelEntryRes;
import eco.ywhc.xr.common.model.entity.ChannelEntry;
import eco.ywhc.xr.core.mapper.ChannelEntryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

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

    @Override
    public long createOne(@NonNull ChannelEntryReq req, long clueId) {
        ChannelEntry channelEntry = channelEntryConverter.fromRequest(req);
        channelEntry.setClueId(clueId);
        channelEntryMapper.insert(channelEntry);
        return channelEntry.getId();
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
        return channelEntryConverter.toResponse(findEntityByClueId(clueId));
    }

    @Override
    public void logicDeleteEntityByClueId(long clueId) {
        ChannelEntry entity = findEntityByClueId(clueId);
        channelEntryMapper.logicDeleteEntityById(entity.getId());
    }

}
