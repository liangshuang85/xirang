package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.FileOwnerType;
import eco.ywhc.xr.common.converter.ProjectInformationConverter;
import eco.ywhc.xr.common.model.dto.req.ProjectInformationReq;
import eco.ywhc.xr.common.model.dto.req.ProjectReq;
import eco.ywhc.xr.common.model.dto.res.AttachmentResponse;
import eco.ywhc.xr.common.model.dto.res.ProjectRes;
import eco.ywhc.xr.common.model.entity.Project;
import eco.ywhc.xr.common.model.entity.ProjectInformation;
import eco.ywhc.xr.core.mapper.ProjectInformationMapper;
import eco.ywhc.xr.core.mapper.ProjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectManagerImpl implements ProjectManager {

    private final ProjectMapper projectMapper;

    private final ProjectInformationMapper projectInformationMapper;

    private final ProjectInformationConverter projectInformationConverter;

    private final AttachmentManager attachmentManager;

    @Override
    public Project findEntityById(@NonNull Long id) {
        return projectMapper.findEntityById(id);
    }

    @Override
    public void createProjectInformation(ProjectInformationReq req, long id) {
        ProjectInformation projectInformation = projectInformationConverter.fromRequest(req);
        projectInformation.setProjectId(id);
        projectInformationMapper.insert(projectInformation);
    }

    @Override
    public void updateProjectInformation(ProjectInformationReq req, long id) {
        ProjectInformation projectInformation = getProjectInformationByProjectId(id);
        projectInformationConverter.update(req, projectInformation);
        projectInformation.setProjectId(id);
        projectInformationMapper.updateById(projectInformation);
    }

    @Override
    public void logicDeleteProjectInformation(long id) {
        projectInformationMapper.logicDeleteEntityById(getProjectInformationByProjectId(id).getId());
    }

    @Override
    public ProjectInformation getProjectInformationByProjectId(long id) {
        QueryWrapper<ProjectInformation> qw = new QueryWrapper<>();
        qw.lambda().eq(ProjectInformation::getDeleted, 0)
                .eq(ProjectInformation::getProjectId, id);
        return projectInformationMapper.selectOne(qw);
    }

    @Override
    public void compareAndUpdateAttachments(ProjectReq req, long id) {
        Set<Long> meetingResolutionsAttachmentIds = req.getMeetingResolutionsAttachmentIds();
        attachmentManager.compareAndUpdate(id, meetingResolutionsAttachmentIds, FileOwnerType.MEETING_RESOLUTION);
        Set<Long> meetingMinutesAttachmentIds = req.getMeetingMinutesAttachmentIds();
        attachmentManager.compareAndUpdate(id, meetingMinutesAttachmentIds, FileOwnerType.MEETING_MINUTES);
        Set<Long> investmentAgreementAttachmentIds = req.getInvestmentAgreementAttachmentIds();
        attachmentManager.compareAndUpdate(id, investmentAgreementAttachmentIds, FileOwnerType.INVESTMENT_AGREEMENT);
        Set<Long> investmentAgreementSigningAttachmentIds = req.getInvestmentAgreementSigningAttachmentIds();
        attachmentManager.compareAndUpdate(id, investmentAgreementSigningAttachmentIds, FileOwnerType.INVESTMENT_AGREEMENT_SIGNING);
        Set<Long> enterpriseInvestmentRecordAttachmentIds = req.getEnterpriseInvestmentRecordAttachmentIds();
        attachmentManager.compareAndUpdate(id, enterpriseInvestmentRecordAttachmentIds, FileOwnerType.ENTERPRISE_INVESTMENT_RECORD);
        Set<Long> landUseRightCertificateAttachmentIds = req.getLandUseRightCertificateAttachmentIds();
        attachmentManager.compareAndUpdate(id, landUseRightCertificateAttachmentIds, FileOwnerType.LAND_USE_RIGHT_CERTIFICATE);
        Set<Long> planningPermitForConstructionLandAttachmentIds = req.getPlanningPermitForConstructionLandAttachmentIds();
        attachmentManager.compareAndUpdate(id, planningPermitForConstructionLandAttachmentIds, FileOwnerType.PLANNING_PERMIT_FOR_CONSTRUCTION_LAND);
        Set<Long> preFeasibilityStudyAttachmentIds = req.getPreFeasibilityStudyAttachmentIds();
        attachmentManager.compareAndUpdate(id, preFeasibilityStudyAttachmentIds, FileOwnerType.PRE_FEASIBILITY_STUDY);
        Set<Long> feasibilityStudyAttachmentIds = req.getFeasibilityStudyAttachmentIds();
        attachmentManager.compareAndUpdate(id, feasibilityStudyAttachmentIds, FileOwnerType.FEASIBILITY_STUDY);
        Set<Long> preliminaryDesigneAttachmentIds = req.getPreliminaryDesignAttachmentIds();
        attachmentManager.compareAndUpdate(id, preliminaryDesigneAttachmentIds, FileOwnerType.PRELIMINARY_DESIGN);
    }

    @Override
    public void findAndSetAttachments(ProjectRes res) {
        long id = res.getId();
        List<AttachmentResponse> meetingResolutionAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.MEETING_RESOLUTION);
        res.setMeetingResolutionAttachments(meetingResolutionAttachments);
        List<AttachmentResponse> meetingMinutesAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.MEETING_MINUTES);
        res.setMeetingMinutesAttachments(meetingMinutesAttachments);
        List<AttachmentResponse> investmentAgreementAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.INVESTMENT_AGREEMENT);
        res.setInvestmentAgreementAttachments(investmentAgreementAttachments);
        List<AttachmentResponse> investmentAgreementSigningAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.INVESTMENT_AGREEMENT_SIGNING);
        res.setInvestmentAgreementSigningAttachments(investmentAgreementSigningAttachments);
        List<AttachmentResponse> enterpriseInvestmentRecordAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.ENTERPRISE_INVESTMENT_RECORD);
        res.setEnterpriseInvestmentRecordAttachments(enterpriseInvestmentRecordAttachments);
        List<AttachmentResponse> landUseRightCertificateAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.LAND_USE_RIGHT_CERTIFICATE);
        res.setLandUseRightCertificateAttachments(landUseRightCertificateAttachments);
        List<AttachmentResponse> planningPermitForConstructionLandAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.PLANNING_PERMIT_FOR_CONSTRUCTION_LAND);
        res.setPlanningPermitForConstructionLandAttachments(planningPermitForConstructionLandAttachments);
        List<AttachmentResponse> preFeasibilityStudyAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.PRE_FEASIBILITY_STUDY);
        res.setPreFeasibilityStudyAttachments(preFeasibilityStudyAttachments);
        List<AttachmentResponse> feasibilityStudyAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.FEASIBILITY_STUDY);
        res.setFeasibilityStudyAttachments(feasibilityStudyAttachments);
        List<AttachmentResponse> preliminaryDesigneAttachments = attachmentManager.findManyByOwnerId(id, FileOwnerType.PRELIMINARY_DESIGN);
        res.setPreliminaryDesignAttachments(preliminaryDesigneAttachments);
    }

    @Override
    public boolean isExistByFrameworkAgreementId(long frameworkAgreementId) {
        QueryWrapper<Project> qw = new QueryWrapper<>();
        qw.lambda().eq(Project::getDeleted, 0)
                .eq(Project::getFrameworkAgreementId, frameworkAgreementId);
        return projectMapper.exists(qw);
    }

}
