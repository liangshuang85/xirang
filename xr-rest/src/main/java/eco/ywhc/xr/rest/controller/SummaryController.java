package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.common.model.dto.res.ProvinceResourceCountRes;
import eco.ywhc.xr.core.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 概况接口
 */
@RestController
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    /**
     * 统计每个省份的线索、框架协议项目和项目的数量
     */
    @GetMapping("/summaries/provinceResourceSummary")
    public List<ProvinceResourceCountRes> summarizeProvinceResource() {
        return summaryService.summarizeProvinceResource();
    }

}
