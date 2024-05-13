package eco.ywhc.xr.common.model.query;

import eco.ywhc.xr.common.constant.FrameworkAgreementType;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.query.BasePageQuery;

/**
 * 框架协议项目query
 */
@Getter
@Setter
@ToString

public class FrameworkAgreementQuery extends BasePageQuery {

    /**
     * 框架协议项目状态
     */
    private FrameworkAgreementType status;

    /**
     * 所属行政区划代码
     */
    private Long adcode;

    /**
     * 负责人的飞书OpenID
     */
    @Size(max = 255)
    private String assigneeId;

    /**
     * 框架协议项目名称
     */
    private String name;

}
