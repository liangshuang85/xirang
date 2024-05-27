package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import eco.ywhc.xr.common.constant.KeypairGeneratorAlgorithm;
import lombok.Data;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 应用
 */
@Data
@TableName("s_application")
public class Application extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = -5075111892364546514L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 应用SID
     */
    private String applicationSid;

    /**
     * 应用名称
     */
    String name;

    /**
     * 描述
     */
    String description;

    /**
     * 公钥
     */
    private String publicKey = "";

    /**
     * 密钥对生成算法
     */
    KeypairGeneratorAlgorithm algorithm;

    /**
     * 启用状态
     */
    private Boolean enabled = true;

}
