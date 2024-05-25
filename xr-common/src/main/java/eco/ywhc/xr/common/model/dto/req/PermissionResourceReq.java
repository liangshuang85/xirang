package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 权限资源Request
 */
@Value
public class PermissionResourceReq implements BaseRestRequest {

    /**
     * 名称
     */
    @NotBlank
    String name;

    /**
     * 编码。如果未填写编码，则由系统自动生成
     */
    @NotBlank
    String code;

    /**
     * 描述
     */
    String description;

}

