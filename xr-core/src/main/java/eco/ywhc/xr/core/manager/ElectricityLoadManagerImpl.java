package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.converter.ElectricityLoadConverter;
import eco.ywhc.xr.common.model.dto.req.ElectricityLoadReq;
import eco.ywhc.xr.common.model.dto.res.ElectricityLoadRes;
import eco.ywhc.xr.common.model.entity.BaseEntity;
import eco.ywhc.xr.common.model.entity.ElectricityLoad;
import eco.ywhc.xr.core.mapper.ElectricityLoadMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ElectricityLoadManagerImpl implements ElectricityLoadManager {

    private final ElectricityLoadMapper electricityLoadMapper;

    private final ElectricityLoadConverter electricityLoadConverter;

    @Override
    public void createMany(List<ElectricityLoadReq> req, long refId) {
        List<ElectricityLoad> electricityLoads = req.stream()
                .map(electricityLoadConverter::fromRequest)
                .peek(electricityLoad -> electricityLoad.setRefId(refId))
                .toList();
        if (electricityLoads.isEmpty()) {
            return;
        }
        electricityLoadMapper.bulkInsert(electricityLoads);
    }

    @Override
    public List<ElectricityLoadRes> findManyByRefId(long refId) {
        QueryWrapper<ElectricityLoad> qw = new QueryWrapper<>();
        qw.lambda().eq(BaseEntity::getDeleted, false)
                .eq(ElectricityLoad::getRefId, refId);
        return electricityLoadMapper.selectList(qw).stream()
                .map(electricityLoadConverter::toResponse)
                .toList();
    }

    @Override
    public void compareAndUpdate(List<ElectricityLoadReq> req, long refId) {
        List<Long> reqIds = req.stream().map(ElectricityLoadReq::getId).toList();
        List<Long> needToDelete = findManyByRefId(refId).stream()
                .map(ElectricityLoadRes::getId)
                .filter(id -> !reqIds.contains(id))
                .toList();
        if (!needToDelete.isEmpty()) {
            bulkLogicDelete(needToDelete);
        }

        List<ElectricityLoadReq> needToInsert = new ArrayList<>();
        req.forEach(i -> {
            ElectricityLoad electricityLoad = electricityLoadMapper.findEntityById(i.getId());
            if (electricityLoad == null) {
                needToInsert.add(i);
            } else {
                electricityLoadConverter.update(i, electricityLoad);
                electricityLoadMapper.updateById(electricityLoad);
            }
        });
        createMany(needToInsert, refId);
    }

    private void bulkLogicDelete(List<Long> ids) {
        UpdateWrapper<ElectricityLoad> uw = new UpdateWrapper<>();
        uw.lambda().in(ElectricityLoad::getId, ids)
                .set(ElectricityLoad::getDeleted, true);
        electricityLoadMapper.update(null, uw);
    }

}
