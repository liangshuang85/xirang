package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 渠道录入信息
 */
@Getter
@Setter
@TableName("b_channel_entry")
public class ChannelEntry extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = -8867367104509501448L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 关联对象ID
     */
    private Long refId;

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
