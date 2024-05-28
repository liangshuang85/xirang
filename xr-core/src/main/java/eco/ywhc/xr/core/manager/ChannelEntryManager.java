package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.req.ChannelEntryReq;
import eco.ywhc.xr.common.model.dto.res.ChannelEntryRes;
import org.springframework.transaction.annotation.Transactional;

/**
 * 渠道录入信息
 */
@Transactional(rollbackFor = {Exception.class})
public interface ChannelEntryManager {

    /**
     * 创建渠道录入信息
     *
     * @param req   渠道录入信息
     * @param refId 关联ID
     */
    void createChannelEntry(ChannelEntryReq req, long refId);

    /**
     * 更新渠道录入信息
     *
     * @param req   渠道录入信息
     * @param refId 关联ID
     */
    void updateChannelEntry(ChannelEntryReq req, long refId);

    /**
     * 逻辑删除渠道录入信息
     *
     * @param refId 关联ID
     */
    void logicDeleteChannelEntry(long refId);

    /**
     * 根据关联ID获取渠道录入信息
     *
     * @param refId 关联ID
     * @return 渠道录入信息
     */
    ChannelEntryRes getChannelEntryByRefId(long refId);

}

