package eco.ywhc.xr.common.model.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 各个省份线索、项目框架协议、项目数量
 */
@Data
@AllArgsConstructor(staticName = "of")
public class ProvinceResourceCountRes {

    /**
     * 6位数字格式的省份行政区划代码
     */
    private Long adcode;

    /**
     * 省份名称
     */
    private String name;

    /**
     * 线索数量
     */
    private Long clueCount;

    /**
     * 框架协议项目数量
     */
    private Long frameworkAgreementCount;

    /**
     * 项目数量
     */
    private Long projectCount;

}
