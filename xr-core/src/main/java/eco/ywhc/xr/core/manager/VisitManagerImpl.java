package eco.ywhc.xr.core.manager;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.converter.VisitConverter;
import eco.ywhc.xr.common.model.dto.req.VisitReq;
import eco.ywhc.xr.common.model.dto.res.VisitRes;
import eco.ywhc.xr.common.model.entity.Visit;
import eco.ywhc.xr.core.mapper.VisitMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 拜访记录(eco.ywhc.xr.common.model.entity.BVisit)表服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VisitManagerImpl implements VisitManager {

    private final VisitMapper visitMapper;

    private final VisitConverter visitConverter;

    @Override
    public Long createOne(@NonNull VisitReq req) {
        Visit visit = visitConverter.fromRequest(req);
        visitMapper.insert(visit);
        return visit.getId();
    }

    @Override
    public int createMany(Collection<VisitReq> visits, long clueId) {
        List<Visit> entities = visits.stream()
                .map(i -> {
                    i.setClueId(clueId);
                    return visitConverter.fromRequest(i);
                })
                .toList();
        int affected = 0;
        if (CollectionUtils.isNotEmpty(visits)) {
            affected = visitMapper.bulkInsert(entities);
        }
        return affected;
    }

    @Override
    public List<Visit> findAllEntitiesByClueId(long clueId) {
        QueryWrapper<Visit> qw = new QueryWrapper<>();
        qw.lambda().eq(Visit::getDeleted, false)
                .eq(Visit::getClueId, clueId);
        return visitMapper.selectList(qw);
    }

    @Override
    public List<VisitRes> findAllByClueId(long clueId) {
        return findAllEntitiesByClueId(clueId).stream().map(visitConverter::toResponse).toList();
    }

    @Override
    public void logicDeleteAllEntitiesByClueId(long clueId) {
        UpdateWrapper<Visit> uw = new UpdateWrapper<>();
        uw.lambda().eq(Visit::getDeleted, false)
                .eq(Visit::getClueId, clueId)
                .set(Visit::getDeleted, true);
        visitMapper.update(null, uw);
    }

    @Override
    public int logicDeleteOne(long id) {
        return visitMapper.logicDeleteEntityById(id);
    }

}
