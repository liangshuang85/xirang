package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 线索录入渠道
 */
@Getter
@Setter
@TableName("b_channel_entry")
public class ChannelEntry extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 1848034602548293788L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 线索ID
     */
    private Long clueId;

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

    /**
     * 线索状态
     */
    private String status = "";

}

