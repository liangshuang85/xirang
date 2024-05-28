package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.ApprovalType;
import eco.ywhc.xr.common.constant.FrameworkAgreementType;
import eco.ywhc.xr.common.constant.TaskType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.BaseRestResponse;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 框架协议项目Res
 */
@Getter
@Setter
@ToString
public class FrameworkAgreementRes implements BaseRestResponse {

    /**
     * 框架协议项目ID
     */
    private Long id;

    /**
     * 框架协议项目名称
     */
    private String name;

    /**
     * 框架协议项目编码
     */
    private String code;

    /**
     * 框架协议项目状态
     */
    private FrameworkAgreementType status;

    /**
     * 行政区
     */
    private AdministrativeDivisionRes administrativeDivision;

    /**
     * 线索ID
     */
    private Long clueId;

    /**
     * 负责人信息
     */
    private AssigneeRes assignee;

    /**
     * 是否已签署框架协议书
     */
    private boolean frameworkAgreementSigned;

    /**
     * 是否已批复项目建议书
     */
    private boolean projectProposalApproved;

    /**
     * 框架协议项目渠道录入信息Res
     */
    ChannelEntryRes frameworkAgreementChannelEntry;

    /**
     * 框架协议关联的任务
     */
    Map<TaskType, Map<String, List<TaskRes>>> taskMap;

    /**
     * 框架协议关联的审批
     */
    Map<ApprovalType, Map<String, List<ApprovalRes>>> approvalMap;

    /**
     * 拜访信息Res
     */
    private List<VisitRes> frameworkVisits;

    /**
     * 项目建议书附件信息
     */
    private List<AttachmentResponse> projectProposalAttachments;

    /**
     * 项目建议书批复附件信息
     */
    private List<AttachmentResponse> projectProposalApprovalAttachments;

    /**
     * 会议决议附件信息
     */
    private List<AttachmentResponse> meetingResolutionAttachments;

    /**
     * 会议纪要附件信息
     */
    private List<AttachmentResponse> meetingMinutesAttachments;

    /**
     * 框架协议附件信息
     */
    private List<AttachmentResponse> frameworkAgreementAttachments;

    /**
     * 框架协议签署附件信息
     */
    private List<AttachmentResponse> frameworkAgreementSigningAttachments;

    /**
     * 创建时间
     */
    private OffsetDateTime createdAt;

    /**
     * 最后修改时间
     */
    private OffsetDateTime updatedAt;

    /**
     * 实例角色成员
     */
    private List<InstanceRoleLarkMemberRes> instanceRoleLarkMembers;

    /**
     * 线索状态变更记录
     */
    private List<ChangeRes> changes;

    /**
     * 基础数据
     */
    private BasicDataRes basicData;

    /**
     * 实例权限编码列表
     */
    private Set<String> permissionCodes = new HashSet<>();

}
