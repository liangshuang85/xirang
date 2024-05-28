package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.constant.ClueStatusType;
import eco.ywhc.xr.common.model.dto.req.ClueReq;
import eco.ywhc.xr.common.model.dto.res.ClueRes;
import eco.ywhc.xr.common.model.entity.Clue;
import eco.ywhc.xr.common.model.query.ClueQuery;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.model.PageableModelSet;
import org.sugar.crud.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 线索
 */
@Transactional(rollbackFor = {Exception.class})
public interface ClueService extends BaseService<Long, Clue, ClueReq, ClueRes, ClueQuery> {

    /**
     * 创建基础信息及有关详情信息
     */
    Long createOne(@NonNull ClueReq req);

    @Override
    PageableModelSet<ClueRes> findMany(@NonNull ClueQuery query);

    /**
     * 查找基础信息详情
     */
    ClueRes findOne(@NonNull Long id);

    /**
     * 更新
     */
    int updateOne(@NonNull Long id, @NonNull ClueReq req);

    /**
     * 根据线索id 删除线索
     *
     * @param id 线索Id
     */
    int logicDeleteOne(@NonNull Long id);

    /**
     * 根据线索ID动态改变线索状态表
     *
     * @param id 线索Id
     */
    Map<ClueStatusType, List<ClueStatusType>> getMap(@NonNull Long id);

}
