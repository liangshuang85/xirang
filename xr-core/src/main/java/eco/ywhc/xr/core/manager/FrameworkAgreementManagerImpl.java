package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.FileOwnerType;
import eco.ywhc.xr.common.converter.FrameworkAgreementChannelEntryConverter;
import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementChannelEntryReq;
import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementReq;
import eco.ywhc.xr.common.model.dto.res.AttachmentResponse;
import eco.ywhc.xr.common.model.dto.res.FrameworkAgreementChannelEntryRes;
import eco.ywhc.xr.common.model.entity.FrameworkAgreement;
import eco.ywhc.xr.common.model.entity.FrameworkAgreementChannelEntry;
import eco.ywhc.xr.core.mapper.FrameworkAgreementChannelEntryMapper;
import eco.ywhc.xr.core.mapper.FrameworkAgreementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class FrameworkAgreementManagerImpl implements FrameworkAgreementManager {

    private final FrameworkAgreementMapper frameworkAgreementMapper;

    private final FrameworkAgreementChannelEntryMapper frameworkAgreementChannelEntryMapper;

    private final FrameworkAgreementChannelEntryConverter frameworkAgreementChannelEntryConverter;

    private final AttachmentManager attachmentManager;

    @Override
    public FrameworkAgreement findEntityById(@NonNull Long id) {
        return frameworkAgreementMapper.findEntityById(id);
    }

    @Override
    public FrameworkAgreementChannelEntry getFrameworkAgreementChannelEntryById(long id) {
        QueryWrapper<FrameworkAgreementChannelEntry> qw = new QueryWrapper<>();
        qw.lambda().eq(FrameworkAgreementChannelEntry::getDeleted, false)
                .eq(FrameworkAgreementChannelEntry::getFrameworkAgreementId, id);
        return frameworkAgreementChannelEntryMapper.selectOne(qw);
    }

    @Override
    public FrameworkAgreementChannelEntryRes getChannelEntryByFrameworkAgreementId(long id) {
        FrameworkAgreementChannelEntry frameworkAgreementChannelEntry = getFrameworkAgreementChannelEntryById(id);
        FrameworkAgreementChannelEntryRes channelEntryRes = frameworkAgreementChannelEntryConverter.toResponse(frameworkAgreementChannelEntry);
        List<AttachmentResponse> projectInfoAttachments = attachmentManager.findManyByOwnerId(channelEntryRes.getId(), FileOwnerType.PROJECT_INFO_STATISTICS);
        List<AttachmentResponse> manyByOwnerId = attachmentManager.findManyByOwnerId(channelEntryRes.getId(), FileOwnerType.PROJECT_FUNDING);
        channelEntryRes.setProjectInfoAttachments(projectInfoAttachments);
        channelEntryRes.setProjectFundingAttachments(manyByOwnerId);
        return channelEntryRes;
    }

    @Override
    public void compareAndUpdateAttachments(Object req, long id) {
        if (req instanceof FrameworkAgreementReq frameworkAgreementReq) {
            Set<Long> projectProposalAttachmentIds = frameworkAgreementReq.getProjectProposalAttachmentIds();
            attachmentManager.compareAndUpdate(id, projectProposalAttachmentIds, FileOwnerType.PROJECT_PROPOSAL);
            Set<Long> projectProposalApprovalAttachmentIds = frameworkAgreementReq.getProjectProposalApprovalAttachmentIds();
            attachmentManager.compareAndUpdate(id, projectProposalApprovalAttachmentIds, FileOwnerType.PROJECT_PROPOSAL_APPROVAL);
            Set<Long> meetingResolutionsAttachmentIds = frameworkAgreementReq.getMeetingResolutionsAttachmentIds();
            attachmentManager.compareAndUpdate(id, meetingResolutionsAttachmentIds, FileOwnerType.MEETING_RESOLUTION);
            Set<Long> meetingMinutesAttachmentIds = frameworkAgreementReq.getMeetingMinutesAttachmentIds();
            attachmentManager.compareAndUpdate(id, meetingMinutesAttachmentIds, FileOwnerType.MEETING_MINUTES);
            Set<Long> frameworkAgreementAttachmentIds = frameworkAgreementReq.getFrameworkAgreementAttachmentIds();
            attachmentManager.compareAndUpdate(id, frameworkAgreementAttachmentIds, FileOwnerType.FRAMEWORK_AGREEMENT);
            Set<Long> frameworkAgreementSigningAttachmentIds = frameworkAgreementReq.getFrameworkAgreementSigningAttachmentIds();
            attachmentManager.compareAndUpdate(id, frameworkAgreementSigningAttachmentIds, FileOwnerType.FRAMEWORK_AGREEMENT_SIGNING);
        } else if (req instanceof FrameworkAgreementChannelEntryReq frameworkAgreementChannelEntryReq) {
            Set<Long> projectFundingAttachmentIds = frameworkAgreementChannelEntryReq.getProjectFundingAttachmentIds();
            attachmentManager.compareAndUpdate(id, projectFundingAttachmentIds, FileOwnerType.PROJECT_FUNDING);
            Set<Long> projectInfoAttachmentIds = frameworkAgreementChannelEntryReq.getProjectInfoAttachmentIds();
            attachmentManager.compareAndUpdate(id, projectInfoAttachmentIds, FileOwnerType.PROJECT_INFO_STATISTICS);
        } else {
            log.error("Unknown req type: {}", req.getClass());
            throw new InternalErrorException("不支持该类型");
        }
    }

    @Override
    public void compareAndUpdateStatus(FrameworkAgreement frameworkAgreement) {
        int count = 0;
        // 判断项目建议书批复状态
        boolean projectProposalApproved = !attachmentManager.findManyEntitiesByOwnerId(frameworkAgreement.getId(), FileOwnerType.PROJECT_PROPOSAL_APPROVAL).isEmpty();
        if (projectProposalApproved != frameworkAgreement.isProjectProposalApproved()) {
            frameworkAgreement.setProjectProposalApproved(projectProposalApproved);
            count++;
        }
        // 判断框架协议书签署状态
        boolean frameworkAgreementSigned = !attachmentManager.findManyEntitiesByOwnerId(frameworkAgreement.getId(), FileOwnerType.FRAMEWORK_AGREEMENT_SIGNING).isEmpty();
        if (frameworkAgreementSigned != frameworkAgreement.isFrameworkAgreementSigned()) {
            frameworkAgreement.setFrameworkAgreementSigned(frameworkAgreementSigned);
            count++;
        }
        if (count > 0) {
            frameworkAgreementMapper.updateById(frameworkAgreement);
        }
    }

}
