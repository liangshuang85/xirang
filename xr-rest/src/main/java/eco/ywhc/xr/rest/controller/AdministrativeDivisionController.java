package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.common.model.dto.res.AdministrativeDivisionRes;
import eco.ywhc.xr.common.model.query.AdministrativeDivisionQuery;
import eco.ywhc.xr.core.manager.AdministrativeDivisionManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 行政区划接口
 */
@RestController
@RequiredArgsConstructor
public class AdministrativeDivisionController {

    private final AdministrativeDivisionManager administrativeDivisionManager;

    /**
     * 获取行政区列表
     */
    @GetMapping("/administrativeDivisions")
    public List<AdministrativeDivisionRes> findMany(@ParameterObject @Valid AdministrativeDivisionQuery query) {
        return administrativeDivisionManager.findMany(query);
    }

    /**
     * 获取指定行政区
     *
     * @param adcode 行政区划代码
     */
    @GetMapping("/administrativeDivisions/{adcode}")
    public AdministrativeDivisionRes findByAdcode(@PathVariable Long adcode) {
        return administrativeDivisionManager.findByAdcode(adcode);
    }

}
