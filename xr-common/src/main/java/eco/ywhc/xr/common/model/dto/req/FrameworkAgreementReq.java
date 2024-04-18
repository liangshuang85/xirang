package eco.ywhc.xr.common.model.dto.req;

import eco.ywhc.xr.common.constant.FrameworkAgreementType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 框架协议项目Req
 */
@Getter
@Setter
@ToString
public class FrameworkAgreementReq implements BaseRestRequest {

    /**
     * 框架协议项目名称
     */
    @NotBlank
    @Size(max = 255)
    private String name;

    /**
     * 框架协议项目状态
     */
    private FrameworkAgreementType status;

    /**
     * 所属行政区划代码
     */
    @NotNull
    private Long adcode;

    /**
     * 线索ID
     */
    @NotNull
    private Long clueId;

    /**
     * 负责人的飞书OpenID
     */
    @NotBlank
    @Size(max = 255)
    private String assigneeId;

    /**
     * 框架协议项目项目信息
     */
    @Valid
    @NotNull
    FrameworkAgreementProjectReq frameworkAgreementProject;

    /**
     * 框架协议项目渠道录入信息
     */
    FrameworkAgreementChannelEntryReq frameworkAgreementChannelEntry;

    /**
     * 框架协议项目项目收资信息
     */
    FrameworkAgreementProjectFundingReq frameworkAgreementProjectFunding;


}
