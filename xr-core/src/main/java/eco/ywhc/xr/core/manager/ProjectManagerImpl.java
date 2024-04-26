package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.FileOwnerType;
import eco.ywhc.xr.common.model.dto.req.ProjectReq;
import eco.ywhc.xr.common.model.entity.Project;
import eco.ywhc.xr.common.model.entity.ProjectInformation;
import eco.ywhc.xr.core.mapper.ProjectInformationMapper;
import eco.ywhc.xr.core.mapper.ProjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectManagerImpl implements ProjectManager {

    private final ProjectMapper projectMapper;

    private final ProjectInformationMapper projectInformationMapper;

    private final AttachmentManager attachmentManager;

    @Override
    public Project findEntityById(@NonNull Long id) {
        return projectMapper.findEntityById(id);
    }

    @Override
    public ProjectInformation getProjectInformationByProjectId(long id) {
        QueryWrapper<ProjectInformation> qw = new QueryWrapper<>();
        qw.lambda().eq(ProjectInformation::getDeleted, 0)
                .eq(ProjectInformation::getProjectId, id);
        return projectInformationMapper.selectOne(qw);
    }

    @Override
    public void linkAttachments(ProjectReq req, long id) {
        Set<Long> meetingResolutionsAttachmentIds = req.getMeetingResolutionsAttachmentIds();
        attachmentManager.update(meetingResolutionsAttachmentIds, FileOwnerType.MEETING_RESOLUTION, id);
        Set<Long> meetingMinutesAttachmentIds = req.getMeetingMinutesAttachmentIds();
        attachmentManager.update(meetingMinutesAttachmentIds, FileOwnerType.MEETING_MINUTES, id);
        Set<Long> investmentAgreementAttachmentIds = req.getInvestmentAgreementAttachmentIds();
        attachmentManager.update(investmentAgreementAttachmentIds, FileOwnerType.INVESTMENT_AGREEMENT, id);
        Set<Long> investmentAgreementSigningAttachmentIds = req.getInvestmentAgreementSigningAttachmentIds();
        attachmentManager.update(investmentAgreementSigningAttachmentIds, FileOwnerType.INVESTMENT_AGREEMENT_SIGNING, id);
        Set<Long> enterpriseInvestmentRecordAttachmentIds = req.getEnterpriseInvestmentRecordAttachmentIds();
        attachmentManager.update(enterpriseInvestmentRecordAttachmentIds, FileOwnerType.ENTERPRISE_INVESTMENT_RECORD, id);
    }

}
