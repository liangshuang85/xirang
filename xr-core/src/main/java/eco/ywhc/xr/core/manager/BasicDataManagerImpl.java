package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.model.entity.BaseEntity;
import eco.ywhc.xr.common.model.entity.BasicData;
import eco.ywhc.xr.core.mapper.BasicDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicDataManagerImpl implements BasicDataManager {

    private final BasicDataMapper basicDataMapper;

    @Override
    public BasicData findEntityByRefId(long refId) {
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
