package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.ApprovalType;
import eco.ywhc.xr.common.converter.ClueConverter;
import eco.ywhc.xr.common.event.ClueCreatedEvent;
import eco.ywhc.xr.common.model.dto.req.ClueReq;
import eco.ywhc.xr.common.model.dto.res.*;
import eco.ywhc.xr.common.model.entity.Clue;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.common.model.query.ClueQuery;
import eco.ywhc.xr.core.manager.*;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import eco.ywhc.xr.core.mapper.ClueMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.ConditionNotMetException;
import org.sugar.crud.model.PageableModelSet;

import java.util.*;
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

    private final LarkEmployeeManager larkEmployeeManager;

    @Override
    public Long createOne(@NonNull ClueReq req) {
        List<Clue> effectiveEntityByAdcode = clueManager.findEffectiveEntityByAdcode(req.getAdcode());
        if (CollectionUtils.isNotEmpty(effectiveEntityByAdcode)) {
            throw new ConditionNotMetException("不能重复新建线索");
        }

        Clue clue = clueConverter.fromRequest(req);
        clue.setClueCode(clueManager.generateUniqueId());
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
        List<Long> adIds = new ArrayList<>();
        if (query.getAdcode() != null) {
            adIds = administrativeDivisionManager.findAllEntityIdsSince(query.getAdcode());
            if (CollectionUtils.isEmpty(adIds)) {
                return PageableModelSet.from(query.paging());
            }
        }
        QueryWrapper<Clue> qw = new QueryWrapper<>();
        qw.lambda().eq(Clue::getDeleted, false)
                .eq(StringUtils.isNotBlank(query.getAssigneeId()), Clue::getAssigneeId, query.getAssigneeId())
                .eq(query.getStatus() != null, Clue::getStatus, query.getStatus())
                .in(CollectionUtils.isNotEmpty(adIds), Clue::getAdcode, adIds)
                .orderByDesc(Clue::getId);
        var rows = clueMapper.selectPage(query.paging(), qw);
        if (rows.getRecords().isEmpty()) {
            return PageableModelSet.from(query.paging());
        }

        Set<Long> adcodes = rows.getRecords().stream().map(Clue::getAdcode).collect(Collectors.toSet());
        Map<Long, AdministrativeDivisionRes> administrativeDivisionMap = administrativeDivisionManager.findAllAsMapByAdcodesSurely(adcodes);

        var result = rows.convert(i -> {
            ClueRes res = clueConverter.toResponse(i);
            res.setAdministrativeDivision(administrativeDivisionMap.get(i.getAdcode()));
            if (i.getAssigneeId() != null) {
                LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployee(i.getAssigneeId());
                AssigneeRes assignee = AssigneeRes.builder()
                        .assigneeId(i.getAssigneeId())
                        .assigneeName(larkEmployee.getName())
                        .avatarInfo(larkEmployee.getAvatarInfo())
                        .build();
                res.setAssignee(assignee);
            }
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

        LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployee(clue.getAssigneeId());
        AssigneeRes assignee = AssigneeRes.builder()
                .assigneeId(clue.getAssigneeId())
                .assigneeName(larkEmployee.getName())
                .avatarInfo(larkEmployee.getAvatarInfo())
                .build();
        res.setAssignee(assignee);

        FundingRes funding = fundingManager.findByClueId(id);
        res.setClueFunding(funding);

        ChannelEntryRes channelEntry = channelEntryManager.findByClueId(id);
        res.setClueChannelEntry(channelEntry);

        List<VisitRes> visits = visitManager.findAllByRefId(id);
        res.setClueVisits(visits);

        Map<ApprovalType, List<ApprovalRes>> approvalMap = approvalManager.listApprovalsByRefId(id).stream()
                .peek(i -> {
                    if (i.getApprovalInstanceId() != null) {
                        approvalManager.updateApproval(i);
                    }
                })
                .collect(Collectors.groupingBy(ApprovalRes::getType));
        res.setApprovalMap(approvalMap);

        return res;
    }

    @Override
    public int updateOne(@NonNull Long id, @NonNull ClueReq req) {
        Clue clue = clueManager.mustFoundEntityById(id);
        if (!Objects.equals(req.getAdcode(), clue.getAdcode())) {
            List<Clue> effectiveEntityByAdcode = clueManager.findEffectiveEntityByAdcode(req.getAdcode());
            if (CollectionUtils.isNotEmpty(effectiveEntityByAdcode)) {
                throw new ConditionNotMetException("该地区已经存在线索");
            }
        }

        clueConverter.update(req, clue);
        int affected = clueMapper.updateById(clue);

        channelEntryManager.logicDeleteEntityByClueId(id);
        channelEntryManager.createOne(req.getClueChannelEntry(), id);

        fundingManager.logicDeleteEntityByClueId(id);
        fundingManager.createOne(req.getClueFunding(), id);

        visitManager.logicDeleteAllEntitiesByRefId(id);
        visitManager.createMany(req.getClueVisits(), id);

        return affected;
    }

    @Override
    public int logicDeleteOne(@NonNull Long id) {
        clueManager.mustFoundEntityById(id);
        int affected = clueMapper.logicDeleteEntityById(id);

        channelEntryManager.logicDeleteEntityByClueId(id);
        fundingManager.logicDeleteEntityByClueId(id);
        visitManager.logicDeleteAllEntitiesByRefId(id);
        approvalManager.logicDeleteAllEntitiesByRefId(id);

        return affected;
    }

}
