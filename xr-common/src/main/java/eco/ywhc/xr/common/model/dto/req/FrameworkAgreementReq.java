package eco.ywhc.xr.common.model.dto.req;

import eco.ywhc.xr.common.constant.FrameworkAgreementType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.BaseRestRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 框架协议项目Req
 */
@Getter
@Setter
@ToString
public class FrameworkAgreementReq implements BaseRestRequest {

    /**
     * 框架协议项目名称
     */
    @NotBlank
    @Size(max = 255)
    private String name;

    /**
     * 框架协议项目状态
     */
    private FrameworkAgreementType status;

    /**
     * 所属行政区划代码
     */
    @NotNull
    private Long adcode;

    /**
     * 线索ID
     */
    @NotNull
    private Long clueId;

    /**
     * 负责人的飞书OpenID
     */
    @NotBlank
    @Size(max = 255)
    private String assigneeId;

    /**
     * 框架协议项目项目信息
     */
    @Valid
    @NotNull
    FrameworkAgreementProjectReq frameworkAgreementProject;

    /**
     * 框架协议项目渠道录入信息
     */
    FrameworkAgreementChannelEntryReq frameworkAgreementChannelEntry;

    /**
     * 框架协议项目项目收资信息
     */
    FrameworkAgreementProjectFundingReq frameworkAgreementProjectFunding;

    /**
     * 拜访信息
     */
    @Valid
    @NotNull
    private List<VisitReq> frameworkVisits = new ArrayList<>();

    /**
     * 项目建议书附件ID
     */
    @NotNull
    private Set<Long> projectProposalAttachmentIds = new HashSet<>();

    /**
     * 项目建议书批复附件ID
     */
    @NotNull
    private Set<Long> projectProposalApprovalAttachmentIds = new HashSet<>();

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
     * 框架协议附件ID
     */
    @NotNull
    private Set<Long> frameworkAgreementAttachmentIds = new HashSet<>();

    /**
     * 框架协议签署附件ID
     */
    @NotNull
    private Set<Long> frameworkAgreementSigningAttachmentIds = new HashSet<>();

}
