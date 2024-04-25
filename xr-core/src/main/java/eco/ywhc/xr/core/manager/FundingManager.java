package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.req.FundingReq;
import eco.ywhc.xr.common.model.dto.res.FundingRes;
import eco.ywhc.xr.common.model.entity.Funding;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

/**
 * 项目收资信息(eco.ywhc.xr.common.model.entity.BFunding)表服务接口
 *
 * @author makejava
 * @since 2024-04-18 11:51:08
 */
@Transactional(rollbackFor = {Exception.class})
public interface FundingManager {

    /**
     * 创建项目资助
     *
     * @param clueId 线索Id
     */
    long createOne(@NonNull FundingReq req, long clueId);

    /**
     * 根据线索Id查询项目资助
     *
     * @param clueId 线索Id
     */
    Funding findEntityByClueId(long clueId);

    /**
     * 根据线索Id查询项目资助Req
     *
     * @param clueId 线索id
     */
    FundingRes findByClueId(long clueId);

    /**
     * 根据线索Id删除项目资助
     *
     * @param clueId 线索Id
     */
    void logicDeleteEntityByClueId(long clueId);

}

