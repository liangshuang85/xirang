package eco.ywhc.xr.common.model.query;

import eco.ywhc.xr.common.constant.ProjectStatusType;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.sugar.crud.query.BasePageQuery;

/**
 * 项目Query
 */
@Value
public class ProjectQuery extends BasePageQuery {

    /**
     * 框架协议项目状态
     */
    ProjectStatusType status;

    /**
     * 所属行政区划代码
     */
    Long adcode;

    /**
     * 负责人的飞书OpenID
     */
    @Size(max = 255)
    String assigneeId;

}
