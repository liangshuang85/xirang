package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.req.ClueReq;
import eco.ywhc.xr.common.model.dto.res.ClueRes;
import eco.ywhc.xr.common.model.entity.ChannelEntry;
import eco.ywhc.xr.common.model.entity.Clue;
import eco.ywhc.xr.common.model.query.ClueQuery;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.manager.BaseManager;

import java.io.Serializable;

/**
 * 基础线索信息(eco.ywhc.xr.common.model.entity.BClue)表服务接口
 *
 * @author makejava
 * @since 2024-04-18 11:50:59
 */
@Transactional(rollbackFor = {Exception.class})
public interface ClueManager extends BaseManager<Long, Clue, ClueReq, ClueRes, ClueQuery> {

    /**
     * 根据线索ID查询线索
     *
     * @param id 线索id
     */
    @Override
    Clue findEntityById(@NonNull Long id);

    /**
     * 根据线索id查询 渠道录入信息
     *
     * @param id 线索id
     */
    ChannelEntry getChannelEntryByClueId(long id);

    Serializable getFundingIdByClueId(long id);

    /**
     * 生成唯一线索编号
     */
    String generateUniqueId();

}

