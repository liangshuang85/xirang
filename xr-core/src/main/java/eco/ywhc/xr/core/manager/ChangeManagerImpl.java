package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.converter.ChangeConverter;
import eco.ywhc.xr.common.model.dto.res.ChangeRes;
import eco.ywhc.xr.common.model.entity.Change;
import eco.ywhc.xr.core.mapper.ChangeMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChangeManagerImpl implements ChangeManager {

    private final ChangeMapper changeMapper;

    private final ChangeConverter changeConverter;

    @Override
    public List<ChangeRes> findAllByRefId(long refId) {
        QueryWrapper<Change> qw = new QueryWrapper<>();
        qw.lambda().eq(Change::getRefId, refId);
        List<Change> change = changeMapper.selectList(qw);
        return changeConverter.toResponseList(change);
    }

    @Override
    public void bulkDeleteByRefId(long refId) {
        QueryWrapper<Change> qw = new QueryWrapper<>();
        qw.lambda().eq(Change::getRefId, refId);
        List<Long> changeIds = changeMapper.selectList(qw).stream()
                .map(Change::getId)
                .toList();
        if (CollectionUtils.isNotEmpty(changeIds)) {
            changeMapper.deleteBatchIds(changeIds);
        }
    }

}
