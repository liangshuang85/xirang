package eco.ywhc.xr.common.model.dto.impexp;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

/**
 * 权限资源导入导出POJO
 */
@Value
@Builder(toBuilder = true)
public class PermissionResourceDump {

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

