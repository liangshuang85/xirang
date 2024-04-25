package eco.ywhc.xr.common.model.dto.req;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.sugar.crud.model.BaseRestRequest;

import java.util.HashSet;
import java.util.Set;

/**
 * 项目收资信息Req
 */
@Data
public class FundingReq implements BaseRestRequest {

    /**
     * 风力发电资源
     */
    @NotBlank
    @Size(max = 255)
    private String windPowerResource;

    /**
     * 光伏发电资源
     */
    @NotBlank
    @Size(max = 255)
    private String photovoltaicResource;

    /**
     * 新能源并网电价
     */
    @NotNull
    private Double gridEnergyPrice;

    /**
     * 新能源配套储能标准
     */
    @NotBlank
    @Size(max = 255)
    private String storageStandards;

    /**
     * 风力及光伏发电弃电量
     */
    @NotNull
    private Double energyDiscarded;

    /**
     * 风力及光伏电弃电量
     */
    @NotNull
    private Double civilHeatingPrice;

    /**
     * 民用供暖价格
     */
    @NotNull
    private Double commercialHeatingPrice;

    /**
     * 年供暖时长
     */
    @NotNull
    private Double heatingDuration;

    /**
     * 周边产业基础
     */
    @NotBlank
    @Size(max = 255)
    private String industryBase;

    /**
     * 风力发电时长
     */
    @NotNull
    private Double windOperationHours;

    /**
     * 光伏发电时长
     */
    @NotNull
    private Double pvOperationHours;

    /**
     * 工商业用电综合电价
     */
    @NotNull
    private Double industrialElectricityPrice;

    /**
     * 民用供暖面积
     */
    @NotNull
    private Double civilHeatingArea;

    /**
     * 工商业供暖面积
     */
    @NotNull
    private Double commercialHeatingArea;

    /**
     * 主要用电企业情况附件ID
     */
    @NotNull
    private Set<Long> majorElectricityConsumerAttachmentIds = new HashSet<>();

}
