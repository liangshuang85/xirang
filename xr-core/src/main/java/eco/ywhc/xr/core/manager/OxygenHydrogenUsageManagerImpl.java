package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.converter.OxygenHydrogenUsageConverter;
import eco.ywhc.xr.common.model.dto.req.OxygenHydrogenUsageReq;
import eco.ywhc.xr.common.model.dto.res.OxygenHydrogenUsageRes;
import eco.ywhc.xr.common.model.entity.OxygenHydrogenUsage;
import eco.ywhc.xr.core.mapper.OxygenHydrogenUsageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OxygenHydrogenUsageManagerImpl implements OxygenHydrogenUsageManager {

    private final OxygenHydrogenUsageMapper oxygenHydrogenUsageMapper;

    private final OxygenHydrogenUsageConverter oxygenHydrogenUsageConverter;

    @Override
    public void createMany(List<OxygenHydrogenUsageReq> req, long refId) {
        List<OxygenHydrogenUsage> oxygenHydrogenUsages = req.stream()
                .map(oxygenHydrogenUsageConverter::fromRequest)
                .peek(oxygenHydrogenUsage -> oxygenHydrogenUsage.setRefId(refId))
                .toList();
        if (oxygenHydrogenUsages.isEmpty()) {
            return;
        }
        oxygenHydrogenUsageMapper.bulkInsert(oxygenHydrogenUsages);
    }

    @Override
    public List<OxygenHydrogenUsageRes> findManyByRefId(long refId) {
        QueryWrapper<OxygenHydrogenUsage> qw = new QueryWrapper<>();
        qw.lambda().eq(OxygenHydrogenUsage::getDeleted, false)
                .eq(OxygenHydrogenUsage::getRefId, refId);
        return oxygenHydrogenUsageMapper.selectList(qw).stream()
                .map(oxygenHydrogenUsageConverter::toResponse)
                .toList();
    }

    @Override
    public void compareAndUpdate(List<OxygenHydrogenUsageReq> req, long refId) {
        List<Long> reqIds = req.stream().map(OxygenHydrogenUsageReq::getId).toList();
        List<Long> needToDelete = findManyByRefId(refId).stream()
                .map(OxygenHydrogenUsageRes::getId)
                .filter(id -> !reqIds.contains(id))
                .toList();
        if (!needToDelete.isEmpty()) {
            bulkLogicDelete(needToDelete);
        }

        List<OxygenHydrogenUsageReq> needToInsert = new ArrayList<>();
        req.forEach(i -> {
            OxygenHydrogenUsage oxygenHydrogenUsage = oxygenHydrogenUsageMapper.findEntityById(i.getId());
            if (oxygenHydrogenUsage == null) {
                needToInsert.add(i);
            } else {
                oxygenHydrogenUsageConverter.update(i, oxygenHydrogenUsage);
                oxygenHydrogenUsageMapper.updateById(oxygenHydrogenUsage);
            }
        });
        createMany(needToInsert, refId);
    }

    private void bulkLogicDelete(List<Long> ids) {
        UpdateWrapper<OxygenHydrogenUsage> uw = new UpdateWrapper<>();
        uw.lambda().in(OxygenHydrogenUsage::getId, ids)
                .set(OxygenHydrogenUsage::getDeleted, true);
        oxygenHydrogenUsageMapper.update(null, uw);
    }

}
