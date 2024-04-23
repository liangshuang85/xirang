package eco.ywhc.xr.common.model.dto.res;

import lombok.Data;
import org.sugar.crud.model.BaseRestResponse;

/**
 * 框架协议项目渠道录入信息Res
 */
@Data
public class FrameworkAgreementChannelEntryRes implements BaseRestResponse {

    /**
     * 框架协议项目渠道录入信息ID
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

}