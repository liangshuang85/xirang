package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.FrameworkAgreementType;
import eco.ywhc.xr.common.constant.TaskType;
import eco.ywhc.xr.common.model.entity.Task;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.BaseRestResponse;

import java.util.List;
import java.util.Map;

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
     * 所属行政区划代码
     */
    private Long adcode;

    /**
     * 线索ID
     */
    private Long clueId;

    /**
     * 负责人的飞书OpenID
     */
    private String assigneeId;

    /**
     * 框架协议项目项目信息Res
     */
    FrameworkAgreementProjectRes frameworkAgreementProject;

    /**
     * 框架协议项目渠道录入信息Res
     */
    FrameworkAgreementChannelEntryRes frameworkAgreementChannelEntry;

    /**
     * 框架协议项目项目收资信息Res
     */
    FrameworkAgreementProjectFundingRes frameworkAgreementProjectFunding;

    /**
     * 框架协议关联的任务
     */
    Map<TaskType, List<TaskRes>> taskResMaps;

}
