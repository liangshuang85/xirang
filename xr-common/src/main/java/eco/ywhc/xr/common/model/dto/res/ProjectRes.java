package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.ProjectType;
import eco.ywhc.xr.common.constant.TaskType;
import eco.ywhc.xr.common.model.entity.Task;
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
     * 行政区划代码
     */
    private Long adcode;

    /**
     * 框架协议ID
     */
    private Long frameworkAgreementId;

    /**
     * 负责人的飞书OpenID
     */
    private String assigneeId;

    /**
     * 项目信息Res
     */
    private ProjectInformationRes projectInformation;

    /**
     * 框架协议关联的任务
     */
    Map<TaskType, List<TaskRes>> taskResMaps;

}
