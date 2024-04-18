package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 框架协议项目项目信息
 */
@Getter
@Setter
@TableName(value = "b_framework_agreement_project")
public class FrameworkAgreementProject extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = -8882251406054165944L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 建设规模
     */
    private Double constructionScale;

    /**
     * 风能资源
     */
    private String windResource;

    /**
     * 光伏资源
     */
    private String solarResource;

    /**
     * 土地资源
     */
    private String landResource;

    /**
     * 框架协议项目ID
     */
    private Long frameworkAgreementId;

}
