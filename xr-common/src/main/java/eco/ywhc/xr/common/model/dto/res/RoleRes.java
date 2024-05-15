package eco.ywhc.xr.common.model.dto.res;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.IdentifiableResponse;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * 角色Response
 */
@Getter
@Setter
@ToString
public class RoleRes implements IdentifiableResponse<Long>, Serializable {

    @Serial
    private static final long serialVersionUID = 1495441032375830151L;

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 启用状态
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private OffsetDateTime createdAt;

    /**
     * 最后修改时间
     */
    private OffsetDateTime updatedAt;

}

