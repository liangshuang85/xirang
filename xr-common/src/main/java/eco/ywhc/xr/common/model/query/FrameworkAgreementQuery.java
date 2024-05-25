package eco.ywhc.xr.common.model.query;

import eco.ywhc.xr.common.constant.FrameworkAgreementType;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.sugar.crud.query.BasePageQuery;

/**
 * 框架协议项目Query
 */
@Value
public class FrameworkAgreementQuery extends BasePageQuery {

    /**
     * 框架协议项目状态
     */
    FrameworkAgreementType status;

    /**
     * 所属行政区划代码
     */
    Long adcode;

    /**
     * 负责人的飞书OpenID
     */
    @Size(max = 255)
    String assigneeId;

    /**
     * 框架协议项目名称
     */
    String name;

}
