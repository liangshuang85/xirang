package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.converter.BasicDataConverter;
import eco.ywhc.xr.common.model.dto.req.BasicDataReq;
import eco.ywhc.xr.common.model.dto.res.BasicDataRes;
import eco.ywhc.xr.common.model.entity.BaseEntity;
import eco.ywhc.xr.common.model.entity.BasicData;
import eco.ywhc.xr.core.mapper.BasicDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicDataManagerImpl implements BasicDataManager {

    private final BasicDataMapper basicDataMapper;

    private final BasicDataConverter basicDataConverter;

    private final OxygenHydrogenUsageManager oxygenHydrogenUsageManager;

    private final ElectricityLoadManager electricityLoadManager;

    @Override
    public void createOne(BasicDataReq req, long refId) {
        BasicData basicData = basicDataConverter.fromRequest(req);
        basicData.setRefId(refId);
        basicDataMapper.insert(basicData);
        electricityLoadManager.createMany(req.getElectricityLoads(), basicData.getId());
        oxygenHydrogenUsageManager.createMany(req.getOxygenHydrogenUsages(), basicData.getId());
    }

    @Override
    public void updateOne(BasicDataReq req, long refId) {
        BasicData basicData = findEntityByRefId(refId);
        if (basicData == null) {
            createOne(req, refId);
            return;
        }
        basicDataConverter.update(req, basicData);
        basicDataMapper.updateById(basicData);
        electricityLoadManager.compareAndUpdate(req.getElectricityLoads(), basicData.getId());
        oxygenHydrogenUsageManager.compareAndUpdate(req.getOxygenHydrogenUsages(), basicData.getId());
    }

    @Override
    public BasicDataRes getBasicData(long refId) {
        BasicData basicData = findEntityByRefId(refId);
        if (basicData == null) {
            return new BasicDataRes();
        }
        BasicDataRes res = basicDataConverter.toResponse(basicData);
        res.setElectricityLoads(electricityLoadManager.findManyByRefId(basicData.getId()));
        res.setOxygenHydrogenUsages(oxygenHydrogenUsageManager.findManyByRefId(basicData.getId()));
        return res;
    }

    private BasicData findEntityByRefId(long refId) {
        QueryWrapper<BasicData> qw = new QueryWrapper<>();
        qw.lambda().eq(BaseEntity::getDeleted, false)
                .eq(BasicData::getRefId, refId);
        return basicDataMapper.selectOne(qw);
    }

    @Override
    public void deleteEntityByRefId(long refId) {
        UpdateWrapper<BasicData> uw = new UpdateWrapper<>();
        uw.lambda().eq(BaseEntity::getDeleted, false)
                .eq(BasicData::getRefId, refId)
                .set(BaseEntity::getDeleted, true);
        basicDataMapper.update(uw);
    }

}
