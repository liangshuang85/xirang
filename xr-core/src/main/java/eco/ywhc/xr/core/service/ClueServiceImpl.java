package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.converter.ClueConverter;
import eco.ywhc.xr.common.event.ClueCreatedEvent;
import eco.ywhc.xr.common.model.dto.req.ClueReq;
import eco.ywhc.xr.common.model.dto.res.*;
import eco.ywhc.xr.common.model.entity.Clue;
import eco.ywhc.xr.common.model.query.ClueQuery;
import eco.ywhc.xr.core.manager.*;
import eco.ywhc.xr.core.mapper.ClueMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InvalidInputException;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClueServiceImpl implements ClueService {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final ClueConverter clueConverter;

    private final ClueManager clueManager;

    private final ClueMapper clueMapper;

    private final AdministrativeDivisionManager administrativeDivisionManager;

    private final ApprovalManager approvalManager;

    private final ChannelEntryManager channelEntryManager;

    private final FundingManager fundingManager;

    private final VisitManager visitManager;

    @Override
    public Long createOne(@NonNull ClueReq req) {
        Clue clue = clueConverter.fromRequest(req);
        clue.setClueCode(clueManager.generateUniqueId());
        clue.setAssigneeId("");
        clueMapper.insert(clue);
        Long id = clue.getId();

        channelEntryManager.createOne(req.getClueChannelEntry(), id);
        fundingManager.createOne(req.getClueFunding(), id);
        visitManager.createMany(req.getClueVisits(), id);

        applicationEventPublisher.publishEvent(ClueCreatedEvent.of(clue));

        return id;
    }

    @Override
    public PageableModelSet<ClueRes> findMany(@NonNull ClueQuery query) {
        QueryWrapper<Clue> qw = new QueryWrapper<>();
        qw.lambda().eq(Clue::getDeleted, false)
                .eq(StringUtils.isNotBlank(query.getAssigneeId()), Clue::getAssigneeId, query.getAssigneeId())
                .eq(query.getAdcode() != null, Clue::getAdcode, query.getAdcode())
                .eq(query.getStatus() != null, Clue::getStatus, query.getStatus());
        var rows = clueMapper.selectPage(query.paging(), qw);
        if (rows.getRecords().isEmpty()) {
            return PageableModelSet.from(query.paging());
        }

        Set<Long> adcodes = rows.getRecords().stream().map(Clue::getAdcode).collect(Collectors.toSet());
        Map<Long, AdministrativeDivisionRes> administrativeDivisionMap = administrativeDivisionManager.findAllAsMapByAdcodesSurely(adcodes);

        var result = rows.convert(i -> {
            ClueRes res = clueConverter.toResponse(i);
            res.setAdministrativeDivision(administrativeDivisionMap.get(i.getAdcode()));
            return res;
        });

        return PageableModelSet.from(result);
    }

    @Override
    public ClueRes findOne(@NonNull Long id) {
        Clue clue = clueManager.mustFoundEntityById(id);
        ClueRes res = clueConverter.toResponse(clue);

        AdministrativeDivisionRes administrativeDivision = administrativeDivisionManager.findByAdcodeSurely(clue.getAdcode());
        res.setAdministrativeDivision(administrativeDivision);

        FundingRes funding = fundingManager.findByClueId(id);
        res.setClueFunding(funding);

        ChannelEntryRes channelEntry = channelEntryManager.findByClueId(id);
        res.setClueChannelEntry(channelEntry);

        List<VisitRes> visits = visitManager.findAllByClueId(id);
        res.setClueVisits(visits);

        List<ApprovalRes> approvals = approvalManager.findAllByClueId(id);
        res.setClueApprovals(approvals);

        return res;
    }

    @Override
    public int updateOne(@NonNull Long id, @NonNull ClueReq req) {
        if (CollectionUtils.isNotEmpty(req.getClueVisits())) {
            throw new InvalidInputException("不应该包含拜访信息");
        }

        Clue clue = clueManager.mustFoundEntityById(id);
        clueConverter.update(req, clue);
        int affected = clueMapper.updateById(clue);

        channelEntryManager.logicDeleteEntityByClueId(id);
        fundingManager.createOne(req.getClueFunding(), id);

        fundingManager.logicDeleteEntityByClueId(id);
        fundingManager.createOne(req.getClueFunding(), id);

        return affected;
    }

    @Override
    public int logicDeleteOne(@NonNull Long id) {
        clueManager.mustFoundEntityById(id);
        int affected = clueMapper.logicDeleteEntityById(id);

        channelEntryManager.logicDeleteEntityByClueId(id);
        fundingManager.logicDeleteEntityByClueId(id);
        visitManager.logicDeleteAllEntitiesByClueId(id);
        approvalManager.logicDeleteAllEntitiesByClueId(id);

        return affected;
    }

}
