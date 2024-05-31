package eco.ywhc.xr.common.model.dto.impexp;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

/**
 * 权限导入导出POJO
 */
@Value
@Builder(toBuilder = true)
public class PermissionDump {

    /**
     * 名称
     */
    @NotBlank
    String name;

    /**
     * 编码
     */
    @NotBlank
    String code;

    /**
     * 描述
     */
    String description;

    /**
     * 资源编码
     */
    @NotBlank
    String resourceCode;

    /**
     * 级别
     */
    String level;

}

