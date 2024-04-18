package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.model.entity.FrameworkAgreement;
import eco.ywhc.xr.common.model.entity.FrameworkAgreementChannelEntry;
import eco.ywhc.xr.common.model.entity.FrameworkAgreementProject;
import eco.ywhc.xr.common.model.entity.FrameworkAgreementProjectFunding;
import eco.ywhc.xr.core.mapper.FrameworkAgreementChannelEntryMapper;
import eco.ywhc.xr.core.mapper.FrameworkAgreementMapper;
import eco.ywhc.xr.core.mapper.FrameworkAgreementProjectFundingMapper;
import eco.ywhc.xr.core.mapper.FrameworkAgreementProjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FrameworkAgreementManagerImpl implements FrameworkAgreementManager {

    private final FrameworkAgreementMapper frameworkAgreementMapper;

    private final FrameworkAgreementProjectMapper frameworkAgreementProjectMapper;

    private final FrameworkAgreementProjectFundingMapper frameworkAgreementProjectFundingMapper;

    private final FrameworkAgreementChannelEntryMapper frameworkAgreementChannelEntryMapper;

    @Override
    public FrameworkAgreement findEntityById(@NonNull Long id) {
        return frameworkAgreementMapper.findEntityById(id);
    }

    @Override
    public FrameworkAgreementProject getProjectByFrameworkAgreementId(long id) {
        QueryWrapper<FrameworkAgreementProject> qw = new QueryWrapper<>();
        qw.lambda().eq(FrameworkAgreementProject::getDeleted, false)
                .eq(FrameworkAgreementProject::getFrameworkAgreementId, id);
        return frameworkAgreementProjectMapper.selectOne(qw);
    }

    @Override
    public FrameworkAgreementChannelEntry getChannelEntryByFrameworkAgreementId(long id) {
        QueryWrapper<FrameworkAgreementChannelEntry> qw = new QueryWrapper<>();
        qw.lambda().eq(FrameworkAgreementChannelEntry::getDeleted, false)
                .eq(FrameworkAgreementChannelEntry::getFrameworkAgreementId, id);
        return frameworkAgreementChannelEntryMapper.selectOne(qw);
    }

    @Override
    public FrameworkAgreementProjectFunding getProjectFundingByFrameworkAgreementId(long id) {
        QueryWrapper<FrameworkAgreementProjectFunding> qw = new QueryWrapper<>();
        qw.lambda().eq(FrameworkAgreementProjectFunding::getDeleted, false)
                .eq(FrameworkAgreementProjectFunding::getFrameworkAgreementId, id);
        return frameworkAgreementProjectFundingMapper.selectOne(qw);
    }

}
