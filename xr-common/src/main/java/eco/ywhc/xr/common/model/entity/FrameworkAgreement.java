package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import eco.ywhc.xr.common.constant.FrameworkAgreementType;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 框架协议项目
 */
@Getter
@Setter
@TableName(value = "b_framework_agreement")
public class FrameworkAgreement extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = 6544378039400870402L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 框架协议项目名称
     */
    private String name;

    /**
     * 框架协议项目编码
     */
    private String code;

    /**
     * 框架协议项目状态
     */
    private FrameworkAgreementType status;

    /**
     * 所属行政区划代码
     */
    private Long adcode;

    /**
     * 线索ID
     */
    private Long clueId;

    /**
     * 负责人的飞书OpenID
     */
    private String assigneeId;

}
