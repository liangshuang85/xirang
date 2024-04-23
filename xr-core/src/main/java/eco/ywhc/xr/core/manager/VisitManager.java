package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.req.VisitReq;
import eco.ywhc.xr.common.model.dto.res.VisitRes;
import eco.ywhc.xr.common.model.entity.Visit;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 拜访记录(eco.ywhc.xr.common.model.entity.BVisit)表服务接口
 *
 * @author makejava
 * @since 2024-04-18 11:51:08
 */
@Transactional(rollbackFor = {Exception.class})
public interface VisitManager {

    /**
     * 添加访问记录
     */
    Long createOne(@NonNull VisitReq req);

    /**
     * 创建多个排放记录
     */
    int createMany(Collection<VisitReq> visits, long clueId);

    /**
     * 根据线索id查询拜访记录
     */
    List<Visit> findAllEntitiesByClueId(long clueId);

    /**
     * 根据线索id查询拜访记录
     */
    List<VisitRes> findAllByClueId(long clueId);

    /**
     * 根据线索id删除拜访记录
     */
    void logicDeleteAllEntitiesByClueId(long clueId);

    /**
     * 删除访问记录
     *
     * @param id 访问记录id
     */
    int logicDeleteOne(long id);

}

