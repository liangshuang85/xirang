package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

import java.util.HashSet;
import java.util.Set;

/**
 * 框架协议项目渠道录入信息Req
 */
@Value
public class FrameworkAgreementChannelEntryReq implements BaseRestRequest {

    /**
     * 渠道合伙人姓名
     */
    String partnerName;

    /**
     * 重要工作背景
     */
    String background;

    /**
     * 重要社会关系
     */
    String socialRelations;

    /**
     * 关键联系人姓名
     */
    String contactName;

    /**
     * 政府对接人姓名
     */
    String counterpartName;

    /**
     * 关键联系人职务
     */
    String contactPosition;

    /**
     * 政府对接人职务
     */
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
