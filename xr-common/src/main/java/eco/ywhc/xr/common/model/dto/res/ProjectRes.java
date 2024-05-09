package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.ApprovalType;
import eco.ywhc.xr.common.constant.ProjectStatusType;
import eco.ywhc.xr.common.constant.TaskType;
import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * 项目Res
 */
@Data
public class ProjectRes implements BaseRestResponse {

    /**
     * 项目ID
     */
    private Long id;

    /**
     * 项目名
     */
    private String name;

    /**
     * 项目编码
     */
    private String code;

    /**
     * 项目状态
     */
    private ProjectStatusType status;

    /**
     * 行政区
     */
    private AdministrativeDivisionRes administrativeDivision;

    /**
     * 框架协议ID
     */
    private Long frameworkAgreementId;

    /**
     * 项目信息Res
     */
    private ProjectInformationRes projectInformation;

    /**
     * 项目关联的任务
     */
    Map<TaskType, Map<String, List<TaskRes>>> taskMap;

    /**
     * 负责人信息
     */
    private AssigneeRes assignee;

    /**
     * 项目关联的审批
     */
    Map<ApprovalType, Map<String, List<ApprovalRes>>> approvalMap;

    /**
     * 会议决议附件信息
     */
    private List<AttachmentResponse> meetingResolutionAttachments;

    /**
     * 会议纪要附件信息
     */
    private List<AttachmentResponse> meetingMinutesAttachments;

    /**
     * 投资协议附件信息
     */
    private List<AttachmentResponse> investmentAgreementAttachments;

    /**
     * 投资协议签订附件信息
     */
    private List<AttachmentResponse> investmentAgreementSigningAttachments;

    /**
     * 企业投资备案附件信息
     */
    private List<AttachmentResponse> enterpriseInvestmentRecordAttachments;

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

}
