package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.util.HashSet;
import java.util.Set;

/**
 * 渠道录入信息Req
 */
@Value
public class ChannelEntryReq implements BaseRestRequest {

    /**
     * 渠道合伙人姓名
     */
    @Size(max = 100)
    String partnerName;

    /**
     * 重要工作背景
     */
    @Size(max = 2000)
    String background;

    /**
     * 重要社会关系
     */
    @Size(max = 2000)
    String socialRelations;

    /**
     * 关键联系人姓名
     */
    @Size(max = 100)
    String contactName;

    /**
     * 政府对接人姓名
     */
    @Size(max = 100)
    String counterpartName;

    /**
     * 关键联系人职务
     */
    @Size(max = 200)
    String contactPosition;

    /**
     * 政府对接人职务
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
