package eco.ywhc.xr.common.model.dto.res;

import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 框渠道录入信息Res
 */
@Data
public class ChannelEntryRes implements BaseRestResponse {

    /**
     * 渠道录入信息ID
     */
    private Long id;

    /**
     * 渠道合伙人姓名
     */
    private String partnerName;

    /**
     * 重要工作背景
     */
    private String background;

    /**
     * 重要社会关系
     */
    private String socialRelations;

    /**
     * 关键联系人姓名
     */
    private String contactName;

    /**
     * 政府对接人姓名
     */
    private String counterpartName;

    /**
     * 关键联系人职务
     */
    private String contactPosition;

    /**
     * 政府对接人职务
     */
    private String counterpartPosition;

    /**
     * 项目基本情况统计表附件信息
     */
    private List<AttachmentResponse> projectInfoAttachments = new ArrayList<>();

    /**
     * 项目收资表附件信息
     */
    private List<AttachmentResponse> projectFundingAttachments = new ArrayList<>();

}
