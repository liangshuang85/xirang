package eco.ywhc.xr.common.model.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.IdentifiableResponse;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

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
    @JsonIgnore
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
     * 资源编码
     */
    private String resourceCode;

    /**
     * 资源名称
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String resourceName;

    /**
     * 级别
     */
    private String level;

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

