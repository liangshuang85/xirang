package eco.ywhc.xr.common.model.dto.impexp;

import eco.ywhc.xr.common.constant.InstanceRefType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

/**
 * 实例角色导入导出POJO
 */
@Value
@Builder(toBuilder = true)
public class InstanceRoleDump {

    /**
     * 角色名
     */
    @NotBlank
    String name;

    /**
     * 角色描述
     */
    String description;

    /**
     * 关联对象类型
     */
    @NotNull
    InstanceRefType refType;

}

