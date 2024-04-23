package eco.ywhc.xr.common.model.dto.res;

import lombok.Data;

/**
 * 线索渠道录入Res
 */
@Data
public class ChannelEntryRes {

    /**
     * 线索渠道信息ID
     */
    private Long id;

    /**
     * 合伙人姓名
     */
    private String partnerName;

    /**
     * 工作背景
     */
    private String background;

    /**
     * 社会关系
     */
    private String socialRelations;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 对接人姓名
     */
    private String counterpartName;

    /**
     * 联系人职务
     */
    private String contactPosition;

    /**
     * 对接人姓名
     */
    private String counterpartPosition;

}
