package eco.ywhc.xr.common.model.dto.req;

import eco.ywhc.xr.common.constant.ProjectStatusType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 项目Req
 */
@Value
public class ProjectReq implements BaseRestRequest {

    /**
     * 项目名
     */
    @NotBlank
    @Size(max = 40)
    String name;

    /**
     * 行政区划代码
     */
    @NotNull
    Long adcode;

    /**
     * 框架协议ID
     */
    @NotNull
    Long frameworkAgreementId;

    /**
     * 负责人的飞书OpenID
     */
    @NotBlank
    @Size(max = 40)
    String assigneeId;

    /**
     * 项目状态
     */
    @NotNull
    ProjectStatusType status;

    /**
     * 项目信息Req
     */
    ProjectInformationReq projectInformation;

    /**
     * 会议决议附件ID
     */
    @NotNull
    Set<Long> meetingResolutionsAttachmentIds = new HashSet<>();

    /**
     * 会议纪要附件ID
     */
    @NotNull
    Set<Long> meetingMinutesAttachmentIds = new HashSet<>();

    /**
     * 投资协议附件ID
     */
    @NotNull
    Set<Long> investmentAgreementAttachmentIds = new HashSet<>();

    /**
     * 投资协议签订附件ID
     */
    @NotNull
    Set<Long> investmentAgreementSigningAttachmentIds = new HashSet<>();

    /**
     * 企业投资备案附件ID
     */
    @NotNull
    Set<Long> enterpriseInvestmentRecordAttachmentIds = new HashSet<>();

    /**
     * 国土土地使用证附件ID
     */
    @NotNull
    Set<Long> landUseRightCertificateAttachmentIds = new HashSet<>();

    /**
     * 建设用地规划许可证附件ID
     */
    @NotNull
    Set<Long> planningPermitForConstructionLandAttachmentIds = new HashSet<>();

    /**
     * 预可研附件ID
     */
    @NotNull
    Set<Long> preFeasibilityStudyAttachmentIds = new HashSet<>();

    /**
     * 可研附件ID
     */
    @NotNull
    Set<Long> feasibilityStudyAttachmentIds = new HashSet<>();

    /**
     * 初步设计附件ID
     */
    @NotNull
    Set<Long> preliminaryDesignAttachmentIds = new HashSet<>();

    /**
     * 实例角色成员
     */
    @Valid
    @NotNull
    List<InstanceRoleLarkMemberReq> instanceRoleLarkMembers = new ArrayList<>();

    /**
     * 基础数据
     */
    BasicDataReq basicData;

    /**
     * 拜访信息
     */
    @Valid
    @NotNull
    List<VisitReq> projectVisits = new ArrayList<>();

}
