package eco.ywhc.xr.common.model.dto.res;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.IdentifiableResponse;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 权限资源
 */
@Getter
@Setter
@ToString
public class PermissionResourceRes implements IdentifiableResponse<Long>, Serializable {

    @Serial
    private static final long serialVersionUID = -6561632325487575617L;

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否内置
     */
    private Boolean builtIn;

    /**
     * 创建时间
     */
    private OffsetDateTime createdAt;

    /**
     * 最后修改时间
     */
    private OffsetDateTime updatedAt;

}

