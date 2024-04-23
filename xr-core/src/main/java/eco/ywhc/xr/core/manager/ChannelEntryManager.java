package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.req.ChannelEntryReq;
import eco.ywhc.xr.common.model.dto.res.ChannelEntryRes;
import eco.ywhc.xr.common.model.entity.ChannelEntry;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

/**
 * 渠道录入信息(eco.ywhc.xr.common.model.entity.BChannelEntry)表服务接口
 *
 * @author makejava
 * @since 2024-04-18 11:51:08
 */
@Transactional(rollbackFor = {Exception.class})
public interface ChannelEntryManager {

    long createOne(@NonNull ChannelEntryReq req, long clueId);

    ChannelEntry findEntityByClueId(long clueId);

    ChannelEntryRes findByClueId(long clueId);

    void logicDeleteEntityByClueId(long clueId);

}

