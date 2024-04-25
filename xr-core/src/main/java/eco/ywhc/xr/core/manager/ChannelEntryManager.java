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

    /**
     * 创建新的渠道
     *
     * @param clueId 线索Id
     */
    long createOne(@NonNull ChannelEntryReq req, long clueId);

    /**
     * 根据线索Id查找渠道
     *
     * @param clueId 线索Id
     */
    ChannelEntry findEntityByClueId(long clueId);

    /**
     * 根据线索Id查找渠道并转换成Res
     *
     * @param clueId 线索Id
     */
    ChannelEntryRes findByClueId(long clueId);

    /**
     * 将线索Id对应的渠道删除
     *
     * @param clueId 线索Id
     */
    void logicDeleteEntityByClueId(long clueId);

}

