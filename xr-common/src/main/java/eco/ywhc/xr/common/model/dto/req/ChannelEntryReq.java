package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.util.HashSet;
import java.util.Set;

/**
 * 线索渠道录入Req
 */
@Value
public class ChannelEntryReq implements BaseRestRequest {

    /**
     * 合伙人姓名
     */
    @Size(max = 100)
    String partnerName;

    /**
     * 工作背景
     */
    @Size(max = 255)
    String background;

    /**
     * 社会关系
     */
    @Size(max = 255)
    String socialRelations;

    /**
     * 联系人姓名
     */
    @Size(max = 100)
    String contactName;

    /**
     * 对接人姓名
     */
    @Size(max = 100)
    String counterpartName;

    /**
     * 联系人职务
     */
    @Size(max = 200)
    String contactPosition;

    /**
     * 对接人姓名
     */
    @Size(max = 200)
    String counterpartPosition;

    /**
     * 项目基本情况统计表附件ID
     */
    @NotNull
    Set<Long> projectInfoAttachmentIds = new HashSet<>();

    /**
     * 项目收资表附件ID
     */
    @NotNull
    Set<Long> projectFundingAttachmentIds = new HashSet<>();

}
