package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 权限资源Request
 */
@Getter
@Setter
@ToString
public class PermissionResourceReq implements BaseRestRequest {

    /**
     * 名称
     */
    @NotBlank
    private String name;

    /**
     * 编码。如果未填写编码，则由系统自动生成
     */
    @NotBlank
    private String code;

    /**
     * 描述
     */
    private String description;

}

