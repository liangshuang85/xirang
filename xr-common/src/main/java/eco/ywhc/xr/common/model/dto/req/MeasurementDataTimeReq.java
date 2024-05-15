package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 测算数据时段表请求实体类
 */
@Data
public class MeasurementDataTimeReq implements BaseRestRequest {

    /**
     * 月份
     */
    @NotNull
    private Integer month;

    /**
     * 尖峰时段
     * <p>
     * 支持格式: x-y,z （x,y,z范围为0-23）
     * 例如: 8-10,20-22,23
     */
    @NotBlank
    private String top;

    /**
     * 高峰时段
     * <p>
     * 支持格式: x-y,z （x,y,z范围为0-23）
     * 例如: 8-10,20-22,23
     */
    @NotBlank
    private String peak;

    /**
     * 平段时段
     * <p>
     * 支持格式: x-y,z （x,y,z范围为0-23）
     * 例如: 8-10,20-22,23
     */
    @NotBlank
    private String normal;

    /**
     * 低谷时段
     * <p>
     * 支持格式: x-y,z （x,y,z范围为0-23）
     * 例如: 8-10,20-22,23
     */
    @NotBlank
    private String valley;

    /**
     * 深谷时段
     * <p>
     * 支持格式: x-y,z （x,y,z范围为0-23）
     * 例如: 8-10,20-22,23
     */
    @NotBlank
    private String deep;

}
