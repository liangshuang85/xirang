package eco.ywhc.xr.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.ApprovalType;
import eco.ywhc.xr.common.constant.ClueStatusType;
import eco.ywhc.xr.common.constant.InstanceRefType;
import eco.ywhc.xr.common.converter.ClueConverter;
import eco.ywhc.xr.common.event.ClueCreatedEvent;
import eco.ywhc.xr.common.event.ClueUpdatedEvent;
import eco.ywhc.xr.common.event.StatusChangedEvent;
import eco.ywhc.xr.common.model.RequestContextUser;
import eco.ywhc.xr.common.model.dto.req.ClueReq;
import eco.ywhc.xr.common.model.dto.req.VisitReq;
import eco.ywhc.xr.common.model.dto.res.*;
import eco.ywhc.xr.common.model.entity.Clue;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.common.model.query.ClueQuery;
import eco.ywhc.xr.core.manager.*;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import eco.ywhc.xr.core.mapper.ClueMapper;
import eco.ywhc.xr.core.util.SessionUtils;
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

    private final VisitManager visitManager;

    private final LarkEmployeeManager larkEmployeeManager;

    private final InstanceRoleLarkMemberManager instanceRoleLarkMemberManager;

    private final InstanceRoleManager instanceRoleManager;

    private final ChangeManager changeManager;

    private final BasicDataManager basicDataManager;

    @Override
    public Long createOne(@NonNull ClueReq req) {
        List<Clue> effectiveEntityByAdcode = clueManager.findEffectiveEntityByAdcode(req.getAdcode());
        if (CollectionUtils.isNotEmpty(effectiveEntityByAdcode)) {
            throw new ConditionNotMetException("不能重复新建线索");
        }

        Clue clue = clueConverter.fromRequest(req);
        clue.setClueCode(clueManager.generateUniqueId());

        boolean hasOfficialVisit = req.getClueVisits().stream().anyMatch(VisitReq::getOfficial);
        clue.setHasOfficialVisit(hasOfficialVisit);

        clueMapper.insert(clue);
        Long id = clue.getId();

        basicDataManager.createOne(req.getBasicData(), id);

        channelEntryManager.createOne(req.getClueChannelEntry(), id);
        visitManager.createMany(req.getClueVisits(), id);

        if (CollectionUtils.isNotEmpty(req.getInstanceRoleLarkMembers())) {
            instanceRoleLarkMemberManager.insertInstanceRoleLarkMember(req.getInstanceRoleLarkMembers(), id, InstanceRefType.CLUE);
        }

        applicationEventPublisher.publishEvent(ClueCreatedEvent.of(clue));

        StatusChangedEvent statusChangedEvent = StatusChangedEvent.builder()
                .refId(clue.getId())
                .refType(InstanceRefType.CLUE)
                .before("")
                .after(clue.getStatus().name())
                .operatorId(clue.getAssigneeId())
                .lastModifiedAt(clue.getCreatedAt())
                .build();
        applicationEventPublisher.publishEvent(statusChangedEvent);

        return id;
    }

    @Override
    public PageableModelSet<ClueRes> findMany(@NonNull ClueQuery query) {
        RequestContextUser requestContextUser = SessionUtils.currentUser();

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
                .eq(query.getLevel() != null, Clue::getLevel, query.getLevel())
                .orderByDesc(Clue::getId);
        // 如果没有全局 CLUE:VIEW 权限，则检查实例权限
        if (!SessionUtils.currentUserPermissionCodes().contains("CLUE:VIEW")) {
            String existsStatement = "SELECT 1 FROM `s_instance_role_lark_member` WHERE `deleted`=0 " +
                    " AND `member_id`='" + requestContextUser.getLarkOpenId() + "' " +
                    " AND `ref_type`='CLUE' " +
                    " AND `ref_id`=`b_clue`.id";
            qw.exists(existsStatement);
        }
        var rows = clueMapper.selectPage(query.paging(), qw);
        if (rows.getRecords().isEmpty()) {
            return PageableModelSet.from(query.paging());
        }

        Set<Long> adcodes = rows.getRecords().stream().map(Clue::getAdcode).collect(Collectors.toSet());
        Map<Long, AdministrativeDivisionRes> administrativeDivisionMap = administrativeDivisionManager.findAllAsMapByAdcodesSurely(adcodes);

        List<Long> clueIds = rows.getRecords().stream().map(Clue::getId).toList();
        Map<Long, Set<String>> permissionMap = instanceRoleManager.listPermissionCodesByRefIdsAndMemberId(clueIds, requestContextUser.getLarkOpenId());

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
                res.setPermissionCodes(permissionMap.getOrDefault(i.getId(), Collections.emptySet()));
            }
            return res;
        });

        return PageableModelSet.from(result);
    }

    @Override
    public ClueRes findOne(@NonNull Long id) {
        RequestContextUser requestContextUser = SessionUtils.currentUser();

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

        ChannelEntryRes channelEntry = channelEntryManager.findByClueId(id);
        res.setClueChannelEntry(channelEntry);

        List<VisitRes> visits = visitManager.findAllByRefId(id);
        res.setClueVisits(visits);

        Map<ApprovalType, Map<String, List<ApprovalRes>>> approvalMap = approvalManager.listApprovalsByRefId(id).stream()
                .peek(i -> {
                    if (i.getApprovalInstanceId() != null) {
                        approvalManager.updateApproval(i);
                    }
                })
                .collect(Collectors.groupingBy(
                        ApprovalRes::getType,
                        Collectors.groupingBy(ApprovalRes::getDepartmentName)
                ));
        res.setApprovalMap(approvalMap);

        List<InstanceRoleLarkMemberRes> instanceRoleLarkMemberRes = instanceRoleLarkMemberManager.findInstanceRoleLarkMemberByRefId(id);
        res.setInstanceRoleLarkMembers(instanceRoleLarkMemberRes);

        List<ChangeRes> changes = changeManager.findAllByRefId(id);
        List<ChangeRes> changeRes = changes.stream().peek(i -> i.setOperator(assignee)).toList();
        res.setChanges(changeRes);

        res.setBasicData(basicDataManager.getBasicData(id));

        res.setPermissionCodes(instanceRoleManager.listPermissionCodesByRefIdAndMemberId(id, requestContextUser.getLarkOpenId()));

        return res;
    }

    @Override
    public int updateOne(@NonNull Long id, @NonNull ClueReq req) {
        Clue clue = clueManager.mustFoundEntityById(id);
        ClueStatusType currentStatus = clue.getStatus();
        if (!Objects.equals(req.getAdcode(), clue.getAdcode())) {
            List<Clue> effectiveEntityByAdcode = clueManager.findEffectiveEntityByAdcode(req.getAdcode());
            if (CollectionUtils.isNotEmpty(effectiveEntityByAdcode)) {
                throw new ConditionNotMetException("该地区已经存在线索");
            }
        }
        clueConverter.update(req, clue);

        boolean hasOfficialVisit = req.getClueVisits().stream().anyMatch(VisitReq::getOfficial);
        clue.setHasOfficialVisit(hasOfficialVisit);

        int affected = clueMapper.updateById(clue);

        channelEntryManager.logicDeleteEntityByClueId(id);
        channelEntryManager.createOne(req.getClueChannelEntry(), id);

        visitManager.logicDeleteAllEntitiesByRefId(id);
        visitManager.createMany(req.getClueVisits(), id);

        instanceRoleLarkMemberManager.deleteInstanceRoleLarkMember(id);
        if (CollectionUtils.isNotEmpty(req.getInstanceRoleLarkMembers())) {
            instanceRoleLarkMemberManager.insertInstanceRoleLarkMember(req.getInstanceRoleLarkMembers(), id, InstanceRefType.CLUE);
        }

        // 发布线索已更新事件
        applicationEventPublisher.publishEvent(ClueUpdatedEvent.of(clue));

        // 发布状态变更事件
        if (currentStatus != req.getStatus()) {
            StatusChangedEvent statusChangedEvent = StatusChangedEvent.builder()
                    .refId(clue.getId())
                    .refType(InstanceRefType.CLUE)
                    .before(currentStatus.name())
                    .after(req.getStatus().name())
                    .operatorId(clue.getAssigneeId())
                    .lastModifiedAt(clue.getUpdatedAt())
                    .build();
            applicationEventPublisher.publishEvent(statusChangedEvent);
        }

        basicDataManager.updateOne(req.getBasicData(), id);

        return affected;
    }

    @Override
    public int logicDeleteOne(@NonNull Long id) {
        clueManager.mustFoundEntityById(id);
        int affected = clueMapper.logicDeleteEntityById(id);

        channelEntryManager.logicDeleteEntityByClueId(id);
        visitManager.logicDeleteAllEntitiesByRefId(id);
        approvalManager.logicDeleteAllEntitiesByRefId(id);
        changeManager.bulkDeleteByRefId(id);
        basicDataManager.deleteEntityByRefId(id);
        return affected;
    }

}
