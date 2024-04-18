package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.common.constant.ProjectType;
import eco.ywhc.xr.common.model.ProjectStatus;
import eco.ywhc.xr.common.model.dto.req.ProjectReq;
import eco.ywhc.xr.common.model.dto.res.ProjectRes;
import eco.ywhc.xr.common.model.query.ProjectQuery;
import eco.ywhc.xr.core.service.ProjectService;
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
 * 项目接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * 新建一个项目
     */
    @PostMapping("/projects")
    public CreateResult<Long> createOne(@Valid @RequestBody ProjectReq req) {
        Long id = projectService.createOne(req);
        return CreateResult.of(id);
    }

    /**
     * 获取项目列表，结果以分页形式返回
     *
     * @param query 项目查询条件
     */
    @GetMapping("/projects")
    public PageableModelSet<ProjectRes> findMany(@Valid @ParameterObject ProjectQuery query) {
        return projectService.findMany(query);
    }

    /**
     * 获取指定项目
     *
     * @param id 项目ID
     */
    @GetMapping("/projects/{id}")
    public ProjectRes findOne(@PathVariable Long id) {
        return projectService.findOne(id);
    }

    /**
     * 修改指定项目
     *
     * @param id 项目ID
     */
    @PutMapping("/projects/{id}")
    public OperationResult updateOne(@PathVariable Long id, @Valid @RequestBody ProjectReq req) {
        int affected = projectService.updateOne(id, req);
        return OperationResult.of(affected);
    }

    /**
     * 删除指定项目
     *
     * @param id 项目ID
     */
    @DeleteMapping("/projects/{id}")
    public OperationResult logicDeleteOne(@PathVariable Long id) {
        int affected = projectService.logicDeleteOne(id);
        return OperationResult.of(affected);
    }

    /**
     * 获取项目状态流转表
     * <br>
     * key：项目当前状态
     * <br>
     * value：当前状态可以变更为的状态列表
     */
    @GetMapping("/projects/status")
    public Map<ProjectType, List<ProjectType>> getMap() {
        return ProjectStatus.getMap();
    }

}
