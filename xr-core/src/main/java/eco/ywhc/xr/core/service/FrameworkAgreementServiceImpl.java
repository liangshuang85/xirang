package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.FrameworkAgreementType;
import eco.ywhc.xr.common.converter.FrameworkAgreementChannelEntryConverter;
import eco.ywhc.xr.common.converter.FrameworkAgreementConverter;
import eco.ywhc.xr.common.converter.FrameworkAgreementProjectConverter;
import eco.ywhc.xr.common.converter.FrameworkAgreementProjectFundingConverter;
import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementReq;
import eco.ywhc.xr.common.model.dto.res.FrameworkAgreementChannelEntryRes;
import eco.ywhc.xr.common.model.dto.res.FrameworkAgreementProjectFundingRes;
import eco.ywhc.xr.common.model.dto.res.FrameworkAgreementProjectRes;
import eco.ywhc.xr.common.model.dto.res.FrameworkAgreementRes;
import eco.ywhc.xr.common.model.entity.FrameworkAgreement;
import eco.ywhc.xr.common.model.entity.FrameworkAgreementChannelEntry;
import eco.ywhc.xr.common.model.entity.FrameworkAgreementProject;
import eco.ywhc.xr.common.model.entity.FrameworkAgreementProjectFunding;
import eco.ywhc.xr.common.model.query.FrameworkAgreementQuery;
import eco.ywhc.xr.core.manager.FrameworkAgreementManager;
import eco.ywhc.xr.core.mapper.FrameworkAgreementChannelEntryMapper;
import eco.ywhc.xr.core.mapper.FrameworkAgreementMapper;
import eco.ywhc.xr.core.mapper.FrameworkAgreementProjectFundingMapper;
import eco.ywhc.xr.core.mapper.FrameworkAgreementProjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.crud.model.PageableModelSet;

import java.time.LocalDate;
import java.util.Objects;

import static eco.ywhc.xr.core.service.FrameworkAgreementServiceImpl.CodeGenerator.generateCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class FrameworkAgreementServiceImpl implements FrameworkAgreementService {

    private final FrameworkAgreementMapper frameworkAgreementMapper;

    private final FrameworkAgreementProjectMapper frameworkAgreementProjectMapper;

    private final FrameworkAgreementProjectFundingMapper frameworkAgreementProjectFundingMapper;

    private final FrameworkAgreementChannelEntryMapper frameworkAgreementChannelEntryMapper;

    private final FrameworkAgreementConverter frameworkAgreementConverter;

    private final FrameworkAgreementChannelEntryConverter frameworkAgreementChannelEntryConverter;

    private final FrameworkAgreementProjectFundingConverter frameworkAgreementProjectFundingConverter;

    private final FrameworkAgreementProjectConverter frameworkAgreementProjectConverter;

    private final FrameworkAgreementManager frameworkAgreementManager;

    public static class CodeGenerator {

        private static int number = 1;

        public static String generateCode() {
            int year = LocalDate.now().getYear();
            String code = "KJ" + year + String.format("%03d", number);
            number++;
            return code;
        }

    }

    @Override
    public Long createOne(@NonNull FrameworkAgreementReq req) {
        FrameworkAgreement frameworkAgreement = frameworkAgreementConverter.fromRequest(req);
        frameworkAgreement.setCode(generateCode());
        frameworkAgreement.setStatus(FrameworkAgreementType.PRE_PROJECT);
        frameworkAgreementMapper.insert(frameworkAgreement);

        FrameworkAgreementChannelEntry frameworkAgreementChannelEntry = frameworkAgreementChannelEntryConverter.fromRequest(req.getFrameworkAgreementChannelEntry());
        frameworkAgreementChannelEntry.setFrameworkAgreementId(frameworkAgreement.getId());
        frameworkAgreementChannelEntryMapper.insert(frameworkAgreementChannelEntry);

        FrameworkAgreementProjectFunding frameworkAgreementProjectFunding = frameworkAgreementProjectFundingConverter.fromRequest(req.getFrameworkAgreementProjectFunding());
        frameworkAgreementProjectFunding.setFrameworkAgreementId(frameworkAgreement.getId());
        frameworkAgreementProjectFundingMapper.insert(frameworkAgreementProjectFunding);

        FrameworkAgreementProject frameworkAgreementProject = frameworkAgreementProjectConverter.fromRequest(req.getFrameworkAgreementProject());
        frameworkAgreementProject.setFrameworkAgreementId(frameworkAgreement.getId());
        frameworkAgreementProjectMapper.insert(frameworkAgreementProject);
        return frameworkAgreement.getId();
    }

    @Override
    public PageableModelSet<FrameworkAgreementRes> findMany(@NonNull FrameworkAgreementQuery query) {
        QueryWrapper<FrameworkAgreement> qw = new QueryWrapper<>();
        qw.lambda().eq(FrameworkAgreement::getDeleted, 0)
                .eq(StringUtils.isNotBlank(query.getAssigneeId()), FrameworkAgreement::getAssigneeId, query.getAssigneeId())
                .eq(Objects.nonNull(query.getAdcode()), FrameworkAgreement::getAdcode, query.getAdcode())
                .eq(Objects.nonNull(query.getStatus()), FrameworkAgreement::getStatus, query.getStatus());

        var rows = frameworkAgreementMapper.selectPage(query.paging(true), qw);
        if (CollectionUtils.isEmpty(rows.getRecords())) {
            return PageableModelSet.from(query.paging());
        }

        var results = rows.convert(i -> {
            FrameworkAgreementRes res = frameworkAgreementConverter.toResponse(i);

            FrameworkAgreementProject project = frameworkAgreementManager.getProjectByFrameworkAgreementId(i.getId());
            FrameworkAgreementProjectRes projectRes = frameworkAgreementProjectConverter.toResponse(project);
            res.setFrameworkAgreementProject(projectRes);

            FrameworkAgreementProjectFunding funding = frameworkAgreementManager.getProjectFundingByFrameworkAgreementId(i.getId());
            FrameworkAgreementProjectFundingRes fundingRes = frameworkAgreementProjectFundingConverter.toResponse(funding);
            res.setFrameworkAgreementProjectFunding(fundingRes);

            FrameworkAgreementChannelEntry channelEntry = frameworkAgreementManager.getChannelEntryByFrameworkAgreementId(i.getId());
            FrameworkAgreementChannelEntryRes channelEntryRes = frameworkAgreementChannelEntryConverter.toResponse(channelEntry);
            res.setFrameworkAgreementChannelEntry(channelEntryRes);

            return res;
        });
        return PageableModelSet.from(results);
    }

    @Override
    public FrameworkAgreementRes findOne(@NonNull Long id) {
        FrameworkAgreement frameworkAgreement = frameworkAgreementManager.mustFoundEntityById(id);
        FrameworkAgreementRes res = frameworkAgreementConverter.toResponse(frameworkAgreement);

        FrameworkAgreementProject project = frameworkAgreementManager.getProjectByFrameworkAgreementId(frameworkAgreement.getId());
        FrameworkAgreementProjectRes projectRes = frameworkAgreementProjectConverter.toResponse(project);
        res.setFrameworkAgreementProject(projectRes);

        FrameworkAgreementProjectFunding funding = frameworkAgreementManager.getProjectFundingByFrameworkAgreementId(frameworkAgreement.getId());
        FrameworkAgreementProjectFundingRes fundingRes = frameworkAgreementProjectFundingConverter.toResponse(funding);
        res.setFrameworkAgreementProjectFunding(fundingRes);

        FrameworkAgreementChannelEntry channelEntry = frameworkAgreementManager.getChannelEntryByFrameworkAgreementId(frameworkAgreement.getId());
        FrameworkAgreementChannelEntryRes channelEntryRes = frameworkAgreementChannelEntryConverter.toResponse(channelEntry);
        res.setFrameworkAgreementChannelEntry(channelEntryRes);
        return res;
    }

    @Override
    public int updateOne(@NonNull Long id, @NonNull FrameworkAgreementReq req) {
        FrameworkAgreement frameworkAgreement = frameworkAgreementManager.mustFoundEntityById(id);
        frameworkAgreementConverter.update(req, frameworkAgreement);
        int affected = frameworkAgreementMapper.updateById(frameworkAgreement);

        if (req.getFrameworkAgreementProject() != null) {
            frameworkAgreementProjectMapper.logicDeleteEntityById(frameworkAgreementManager.getProjectByFrameworkAgreementId(id).getId());
            FrameworkAgreementProject frameworkAgreementProject = frameworkAgreementProjectConverter.fromRequest(req.getFrameworkAgreementProject());
            frameworkAgreementProject.setFrameworkAgreementId(frameworkAgreement.getId());
            frameworkAgreementProjectMapper.insert(frameworkAgreementProject);
        }

        if (req.getFrameworkAgreementChannelEntry() != null) {
            frameworkAgreementChannelEntryMapper.logicDeleteEntityById(frameworkAgreementManager.getChannelEntryByFrameworkAgreementId(id).getId());
            FrameworkAgreementChannelEntry frameworkAgreementChannelEntry = frameworkAgreementChannelEntryConverter.fromRequest(req.getFrameworkAgreementChannelEntry());
            frameworkAgreementChannelEntry.setFrameworkAgreementId(frameworkAgreement.getId());
            frameworkAgreementChannelEntryMapper.insert(frameworkAgreementChannelEntry);
        }

        if (req.getFrameworkAgreementProjectFunding() != null) {
            frameworkAgreementProjectFundingMapper.logicDeleteEntityById(frameworkAgreementManager.getProjectFundingByFrameworkAgreementId(id).getId());
            FrameworkAgreementProjectFunding frameworkAgreementProjectFunding = frameworkAgreementProjectFundingConverter.fromRequest(req.getFrameworkAgreementProjectFunding());
            frameworkAgreementProjectFunding.setFrameworkAgreementId(frameworkAgreement.getId());
            frameworkAgreementProjectFundingMapper.insert(frameworkAgreementProjectFunding);
        }
        return affected;
    }

    @Override
    public int logicDeleteOne(@NonNull Long id) {
        frameworkAgreementProjectMapper.logicDeleteEntityById(frameworkAgreementManager.getProjectByFrameworkAgreementId(id).getId());
        frameworkAgreementProjectFundingMapper.logicDeleteEntityById(frameworkAgreementManager.getProjectFundingByFrameworkAgreementId(id).getId());
        frameworkAgreementChannelEntryMapper.logicDeleteEntityById(frameworkAgreementManager.getChannelEntryByFrameworkAgreementId(id).getId());
        return frameworkAgreementMapper.logicDeleteEntityById(id);
    }

}
