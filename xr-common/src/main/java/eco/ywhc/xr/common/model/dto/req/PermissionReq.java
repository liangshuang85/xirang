package eco.ywhc.xr.common.model.dto.req;

import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 权限
 */
@Value
public class PermissionReq implements BaseRestRequest {

    /**
     * 名称
     */
    String name;

    /**
     * 编码
     */
    String code;

    /**
     * 描述
     */
    String description;

    /**
     * 资源编码
     */
    String resourceCode;

    /**
     * 级别
     */
    String level = "";

}

