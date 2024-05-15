package eco.ywhc.xr.common.model.dto.res;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.IdentifiableResponse;

import java.io.Serial;
import java.io.Serializable;

/**
 * 权限
 */
@Getter
@Setter
@ToString
public class PermissionRes implements IdentifiableResponse<Long>, Serializable {

    @Serial
    private static final long serialVersionUID = -4818923159061048836L;

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 资源编码
     */
    private String resourceCode;

    /**
     * 编码
     */
    private String code;

    /**
     * 描述
     */
    private String description;

    /**
     * 级别
     */
    private String level;

}

