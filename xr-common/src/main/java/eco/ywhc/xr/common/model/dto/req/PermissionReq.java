package eco.ywhc.xr.common.model.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 权限
 */
@Getter
@Setter
@ToString
public class PermissionReq implements BaseRestRequest {

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String resourceCode;

    /**
     * 级别
     */
    private String level;

}

