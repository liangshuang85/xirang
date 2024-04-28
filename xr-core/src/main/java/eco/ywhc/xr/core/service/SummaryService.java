package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.res.ProvinceResourceCountRes;

import java.util.List;

/**
 * 概括
 */
public interface SummaryService {

    /**
     * 统计每个省份的线索、框架协议项目和项目的数量
     */
    List<ProvinceResourceCountRes> summarizeProvinceResource();

}
