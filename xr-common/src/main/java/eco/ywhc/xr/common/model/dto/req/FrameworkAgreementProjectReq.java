package eco.ywhc.xr.common.model.dto.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 框架协议项目项目信息Req
 */
@Getter
@Setter
@ToString
public class FrameworkAgreementProjectReq implements BaseRestRequest {

    /**
     * 建设规模
     */
    private Double constructionScale;

    /**
     * 风能资源
     */
    private String windResource;

    /**
     * 光伏资源
     */
    private String solarResource;

    /**
     * 土地资源
     */
    private String landResource;

}
