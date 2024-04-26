package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.ApprovalType;
import eco.ywhc.xr.common.constant.ProjectType;
import eco.ywhc.xr.common.constant.TaskType;
import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

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
    private ProjectType status;

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
     * 框架协议关联的任务
     */
    Map<TaskType, List<TaskRes>> taskMap;

    /**
     * 负责人信息
     */
    private AssigneeRes assignee;

    /**
     * 框架协议关联的审批
     */
    Map<ApprovalType, List<ApprovalRes>> approvalMap;

}
