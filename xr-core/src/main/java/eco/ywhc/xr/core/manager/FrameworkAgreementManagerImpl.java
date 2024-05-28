package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.FileOwnerType;
import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementReq;
import eco.ywhc.xr.common.model.entity.FrameworkAgreement;
import eco.ywhc.xr.core.mapper.FrameworkAgreementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class FrameworkAgreementManagerImpl implements FrameworkAgreementManager {

    private final AttachmentManager attachmentManager;

    private final FrameworkAgreementMapper frameworkAgreementMapper;

    @Override
    public FrameworkAgreement findEntityById(@NonNull Long id) {
        return frameworkAgreementMapper.findEntityById(id);
    }

    @Override
    public void compareAndUpdateAttachments(FrameworkAgreementReq req, long id) {
        Set<Long> projectProposalAttachmentIds = req.getProjectProposalAttachmentIds();
        attachmentManager.compareAndUpdate(id, projectProposalAttachmentIds, FileOwnerType.PROJECT_PROPOSAL);
        Set<Long> projectProposalApprovalAttachmentIds = req.getProjectProposalApprovalAttachmentIds();
        attachmentManager.compareAndUpdate(id, projectProposalApprovalAttachmentIds, FileOwnerType.PROJECT_PROPOSAL_APPROVAL);
        Set<Long> meetingResolutionsAttachmentIds = req.getMeetingResolutionsAttachmentIds();
        attachmentManager.compareAndUpdate(id, meetingResolutionsAttachmentIds, FileOwnerType.MEETING_RESOLUTION);
        Set<Long> meetingMinutesAttachmentIds = req.getMeetingMinutesAttachmentIds();
        attachmentManager.compareAndUpdate(id, meetingMinutesAttachmentIds, FileOwnerType.MEETING_MINUTES);
        Set<Long> frameworkAgreementAttachmentIds = req.getFrameworkAgreementAttachmentIds();
        attachmentManager.compareAndUpdate(id, frameworkAgreementAttachmentIds, FileOwnerType.FRAMEWORK_AGREEMENT);
        Set<Long> frameworkAgreementSigningAttachmentIds = req.getFrameworkAgreementSigningAttachmentIds();
        attachmentManager.compareAndUpdate(id, frameworkAgreementSigningAttachmentIds, FileOwnerType.FRAMEWORK_AGREEMENT_SIGNING);
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

    @Override
    public boolean isExistByClueId(long clueId) {
        QueryWrapper<FrameworkAgreement> qw = new QueryWrapper<>();
        qw.lambda().eq(FrameworkAgreement::getDeleted, false)
                .eq(FrameworkAgreement::getClueId, clueId);
        return frameworkAgreementMapper.exists(qw);
    }

}
