package eco.ywhc.xr.common.model.query;

import eco.ywhc.xr.common.constant.ProjectStatusType;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.query.BasePageQuery;

/**
 * 项目查询条件
 */
@Getter
@Setter
@ToString
public class ProjectQuery extends BasePageQuery {

    /**
     * 框架协议项目状态
     */
    private ProjectStatusType status;

    /**
     * 所属行政区划代码
     */
    private Long adcode;

    /**
     * 负责人的飞书OpenID
     */
    @Size(max = 255)
    private String assigneeId;

}
