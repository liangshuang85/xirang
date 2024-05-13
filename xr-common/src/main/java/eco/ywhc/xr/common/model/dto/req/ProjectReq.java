package eco.ywhc.xr.common.model.dto.req;

import eco.ywhc.xr.common.constant.ProjectStatusType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.sugar.crud.model.BaseRestRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 项目Req
 */
@Data
public class ProjectReq implements BaseRestRequest {

    /**
     * 项目名
     */
    @NotBlank
    @Size(max = 40)
    private String name;

    /**
     * 行政区划代码
     */
    @NotNull
    private Long adcode;

    /**
     * 框架协议ID
     */
    @NotNull
    private Long frameworkAgreementId;

    /**
     * 负责人的飞书OpenID
     */
    @NotBlank
    @Size(max = 40)
    private String assigneeId;

    /**
     * 项目状态
     */
    @NotNull
    private ProjectStatusType status;

    /**
     * 项目信息Req
     */
    private ProjectInformationReq projectInformation;

    /**
     * 会议决议附件ID
     */
    @NotNull
    private Set<Long> meetingResolutionsAttachmentIds = new HashSet<>();

    /**
     * 会议纪要附件ID
     */
    @NotNull
    private Set<Long> meetingMinutesAttachmentIds = new HashSet<>();

    /**
     * 投资协议附件ID
     */
    @NotNull
    private Set<Long> investmentAgreementAttachmentIds = new HashSet<>();

    /**
     * 投资协议签订附件ID
     */
    @NotNull
    private Set<Long> investmentAgreementSigningAttachmentIds = new HashSet<>();

    /**
     * 企业投资备案附件ID
     */
    @NotNull
    private Set<Long> enterpriseInvestmentRecordAttachmentIds = new HashSet<>();

    /**
     * 实例角色成员
     */
    @Valid
    @NotNull
    private List<InstanceRoleLarkMemberReq> instanceRoleLarkMembers = new ArrayList<>();

    /**
     * 基础数据
     */
    private BasicDataReq basicData;

    /**
     * 拜访信息
     */
    @Valid
    @NotNull
    private List<VisitReq> projectVisits = new ArrayList<>();

}
