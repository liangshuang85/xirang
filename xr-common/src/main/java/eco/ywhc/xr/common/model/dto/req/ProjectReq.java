package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 项目Req
 */
@Data
public class ProjectReq implements BaseRestRequest {

    /**
     * 项目名
     */
    @NotBlank
    @Size(max = 40)
    private String name;

    /**
     * 行政区划代码
     */
    @NotNull
    private Long adcode;

    /**
     * 框架协议ID
     */
    @NotNull
    private Long frameworkAgreementId;

    /**
     * 负责人的飞书OpenID
     */
    @NotBlank
    @Size(max = 40)
    private String assigneeId;

    /**
     * 项目信息Req
     */
    private ProjectInformationReq projectInformation;

}
