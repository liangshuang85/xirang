package eco.ywhc.xr.common.model.dto.impexp;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

/**
 * 角色导入导出POJO
 */
@Value
@Builder(toBuilder = true)
public class RoleDump {

    /**
     * 名称
     */
    @NotBlank
    String name;

    /**
     * 描述
     */
    String description;

}

