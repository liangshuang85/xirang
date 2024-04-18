package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;

/**
 * 框架协议项目项目收资信息
 */
@Getter
@Setter
@TableName(value = "b_framework_agreement_project_funding")
public class FrameworkAgreementProjectFunding extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = -7516979355785057978L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 框架协议项目ID
     */
    private Long frameworkAgreementId;

    /**
     * 风力发电资源
     */
    private String windPowerResource;

    /**
     * 光伏发电资源
     */
    private String photovoltaicResource;

    /**
     * 新能源并网电价
     */
    private Double gridEnergyPrice;

    /**
     * 新能源配套储能标准
     */
    private String storageStandards;

    /**
     * 风力及光伏发电弃电量
     */
    private Double energyDiscarded;

    /**
     * 民用供暖价格
     */
    private Double civilHeatingPrice;

    /**
     * 工商业供暖价格
     */
    private Double commercialHeatingPrice;

    /**
     * 年供暖时长
     */
    private Double heatingDuration;

    /**
     * 周边产业基础
     */
    private String industryBase;

    /**
     * 风力发电时长
     */
    private Double windOperationHours;

    /**
     * 光伏发电时长
     */
    private Double pvOperationHours;

    /**
     * 工商业用电综合电价
     */
    private Double industrialElectricityPrice;

    /**
     * 民用供暖面积
     */
    private Double civilHeatingArea;

    /**
     * 工商业供暖面积
     */
    private Double commercialHeatingArea;

}
