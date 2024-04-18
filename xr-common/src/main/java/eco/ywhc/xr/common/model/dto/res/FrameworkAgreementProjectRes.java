package eco.ywhc.xr.common.model.dto.res;

import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

/**
 * 框架协议项目项目信息Res
 */
@Data
public class FrameworkAgreementProjectRes implements BaseRestResponse {

    /**
     * 框架协议项目项目信息ID
     */
    private Long id;

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
