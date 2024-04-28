package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.common.constant.ClueStatusType;
import eco.ywhc.xr.common.model.ClueStatus;
import eco.ywhc.xr.common.model.dto.req.ClueReq;
import eco.ywhc.xr.common.model.dto.res.ClueRes;
import eco.ywhc.xr.common.model.query.ClueQuery;
import eco.ywhc.xr.core.service.ClueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import org.sugar.crud.model.CreateResult;
import org.sugar.crud.model.OperationResult;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;
import java.util.Map;

/**
 * 线索管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ClueController {

    private final ClueService clueService;

    /**
     * 创建新的线索
     */
    @PostMapping("/clues")
    public CreateResult<Long> createOne(@Valid @RequestBody ClueReq clue) {
        Long id = clueService.createOne(clue);
        return CreateResult.of(id);
    }

    /**
     * 获取线索列表，结果以分页形式返回
     */
    @GetMapping("/clues")
    public PageableModelSet<ClueRes> findSimpleMany(@Valid @ParameterObject ClueQuery query) {
        return clueService.findMany(query);
    }

    /**
     * 查询指定ID线索的详情
     *
     * @param id 线索ID
     */
    @GetMapping("/clues/{id}")
    public ClueRes findOne(@PathVariable Long id) {
        return clueService.findOne(id);
    }

    /**
     * 修改线索信息
     *
     * @param id 线索ID
     */
    @PutMapping("/clues/{id}")
    public OperationResult updateOne(@PathVariable Long id, @Valid @RequestBody ClueReq clue) {
        int affected = clueService.updateOne(id, clue);
        return OperationResult.of(affected);
    }

    /**
     * 删除线索
     *
     * @param id 线索ID
     */
    @DeleteMapping("/clues/{id}")
    public OperationResult logicDeleteOne(@PathVariable Long id) {
        int affected = clueService.logicDeleteOne(id);
        return OperationResult.of(affected);
    }

    /**
     * 获取线索状态表
     * <br>
     * key:线索当前状态
     * <br>
     * value：当前状态可以变更为的状态列表
     */
    @GetMapping("/clues/status")
    public Map<ClueStatusType, List<ClueStatusType>> getMap() {
        return ClueStatus.getMap();
    }

}
