package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.constant.FileOwnerType;
import eco.ywhc.xr.common.converter.VisitConverter;
import eco.ywhc.xr.common.model.dto.req.VisitReq;
import eco.ywhc.xr.common.model.dto.res.AttachmentResponse;
import eco.ywhc.xr.common.model.dto.res.VisitRes;
import eco.ywhc.xr.common.model.entity.Visit;
import eco.ywhc.xr.core.mapper.VisitMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InvalidInputException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 拜访记录(eco.ywhc.xr.common.model.entity.BVisit)表服务实现类
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VisitManagerImpl implements VisitManager {

    private final VisitMapper visitMapper;

    private final VisitConverter visitConverter;

    private final AttachmentManager attachmentManager;

    @Override
    public Long createOne(@NonNull VisitReq req) {
        Visit visit = visitConverter.fromRequest(req);
        visitMapper.insert(visit);
        return visit.getId();
    }

    @Override
    public int createMany(Collection<VisitReq> visitReqs, long clueId) {
        for (VisitReq visitReq : visitReqs) {
            Set<Long> invitationLetterAttachmentIds = visitReq.getInvitationLetterAttachmentIds();
            if (CollectionUtils.isNotEmpty(invitationLetterAttachmentIds) &&
                    !attachmentManager.containsAll(invitationLetterAttachmentIds)) {
                throw new InvalidInputException("邀请函附件ID列表错误");
            }

            Set<Long> visitRecordAttachmentIds = visitReq.getVisitRecordAttachmentIds();
            if (CollectionUtils.isNotEmpty(visitRecordAttachmentIds) &&
                    !attachmentManager.containsAll(visitRecordAttachmentIds)) {
                throw new InvalidInputException("拜访记录附件ID列表错误");
            }

            Visit visit = visitConverter.fromRequest(visitReq);
            visitMapper.insert(visit);
            Long id = visit.getId();

            attachmentManager.update(invitationLetterAttachmentIds, FileOwnerType.INVITATION_LETTER, id);
            attachmentManager.update(visitRecordAttachmentIds, FileOwnerType.VISIT_RECORD, id);
        }

        return visitReqs.size();
    }

    @Override
    public List<Visit> findAllEntitiesByClueId(long clueId) {
        QueryWrapper<Visit> qw = new QueryWrapper<>();
        qw.lambda().eq(Visit::getDeleted, false)
                .eq(Visit::getClueId, clueId);
        return visitMapper.selectList(qw);
    }

    @Override
    public List<VisitRes> findAllByClueId(long clueId) {
        List<Visit> visits = findAllEntitiesByClueId(clueId);
        if (CollectionUtils.isEmpty(visits)) {
            return Collections.emptyList();
        }
        Set<Long> visitIds = visits.stream().map(Visit::getId).collect(Collectors.toSet());
        Map<Long, List<AttachmentResponse>> attachmentMap = attachmentManager.findOneToManyByOwnerIds(visitIds);
        return visits.stream()
                .map(visit -> {
                    VisitRes res = visitConverter.toResponse(visit);
                    List<AttachmentResponse> invitationLetterAttachments = attachmentMap.get(visit.getId())
                            .stream().filter(i -> i.getOwnerType() == FileOwnerType.INVITATION_LETTER)
                            .toList();
                    res.setInvitationLetterAttachments(invitationLetterAttachments);
                    List<AttachmentResponse> visitRecordAttachments = attachmentMap.get(visit.getId())
                            .stream().filter(i -> i.getOwnerType() == FileOwnerType.VISIT_RECORD)
                            .toList();
                    res.setVisitRecordAttachments(visitRecordAttachments);
                    return res;
                })
                .toList();
    }

    @Override
    public void logicDeleteAllEntitiesByClueId(long clueId) {
        UpdateWrapper<Visit> uw = new UpdateWrapper<>();
        uw.lambda().eq(Visit::getDeleted, false)
                .eq(Visit::getClueId, clueId)
                .set(Visit::getDeleted, true);
        visitMapper.update(null, uw);
    }

    @Override
    public int logicDeleteOne(long id) {
        return visitMapper.logicDeleteEntityById(id);
    }

}
