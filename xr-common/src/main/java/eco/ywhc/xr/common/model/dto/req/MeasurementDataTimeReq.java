package eco.ywhc.xr.common.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.sugar.crud.model.BaseRestRequest;

/**
 * 测算数据时段表请求实体类
 */
@Value
public class MeasurementDataTimeReq implements BaseRestRequest {

    /**
     * 月份
     */
    @NotNull
    Integer month;

    /**
     * 尖峰时段
     * <p>
     * 支持格式: x-y,z （x,y,z范围为0-23）
     * 例如: 8-10,20-22,23
     */
    @NotBlank
    String top;

    /**
     * 高峰时段
     * <p>
     * 支持格式: x-y,z （x,y,z范围为0-23）
     * 例如: 8-10,20-22,23
     */
    @NotBlank
    String peak;

    /**
     * 平段时段
     * <p>
     * 支持格式: x-y,z （x,y,z范围为0-23）
     * 例如: 8-10,20-22,23
     */
    @NotBlank
    String normal;

    /**
     * 低谷时段
     * <p>
     * 支持格式: x-y,z （x,y,z范围为0-23）
     * 例如: 8-10,20-22,23
     */
    @NotBlank
    String valley;

    /**
     * 深谷时段
     * <p>
     * 支持格式: x-y,z （x,y,z范围为0-23）
     * 例如: 8-10,20-22,23
     */
    @NotBlank
    String deep;

}
