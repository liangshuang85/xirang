package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull
    private Double constructionScale;

    /**
     * 风能资源
     */
    @NotBlank
    @Size(max = 65535)
    private String windResource;

    /**
     * 光伏资源
     */
    @NotBlank
    @Size(max = 65535)
    private String solarResource;

    /**
     * 土地资源
     */
    @NotBlank
    @Size(max = 65535)
    private String landResource;

}
