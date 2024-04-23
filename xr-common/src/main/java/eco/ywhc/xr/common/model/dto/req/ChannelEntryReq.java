package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 线索渠道录入Req
 */
@Data
public class ChannelEntryReq implements BaseRestRequest {

    /**
     * 合伙人姓名
     */
    @NotBlank
    @Size(max = 10)
    private String partnerName;

    /**
     * 工作背景
     */
    @NotBlank
    @Size(max = 255)
    private String background;

    /**
     * 社会关系
     */
    @NotBlank
    @Size(max = 255)
    private String socialRelations;

    /**
     * 联系人姓名
     */
    @NotBlank
    @Size(max = 10)
    private String contactName;

    /**
     * 对接人姓名
     */
    @NotBlank
    @Size(max = 10)
    private String counterpartName;

    /**
     * 联系人职务
     */
    @NotBlank
    @Size(max = 20)
    private String contactPosition;

    /**
     * 对接人姓名
     */
    @NotBlank
    @Size(max = 20)
    private String counterpartPosition;

}