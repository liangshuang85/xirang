package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.req.ProjectReq;
import eco.ywhc.xr.common.model.dto.res.ProjectRes;
import eco.ywhc.xr.common.model.entity.Project;
import eco.ywhc.xr.common.model.query.ProjectQuery;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.model.PageableModelSet;
import org.sugar.crud.service.BaseService;

@Transactional(rollbackFor = {Exception.class})
public interface ProjectService extends BaseService<Long, Project, ProjectReq, ProjectRes, ProjectQuery> {

    /**
     * 新建项目
     *
     * @param req 项目req
     */
    @Override
    Long createOne(@NonNull ProjectReq req);

    /**
     * 获取项目列表，结果以分页形式返回
     *
     * @param query 项目查询条件
     */
    @Override
    PageableModelSet<ProjectRes> findMany(@NonNull ProjectQuery query);

    /**
     * 查找指定项目
     *
     * @param id 项目ID
     */
    @Override
    ProjectRes findOne(@NonNull Long id);

    /**
     * 更新指定项目
     *
     * @param id  项目ID
     * @param req 更新req
     */
    @Override
    int updateOne(@NonNull final Long id, @NonNull ProjectReq req);

    /**
     * 逻辑删除指定项目
     *
     * @param id 项目ID
     */
    int logicDeleteOne(@NonNull final Long id);

}
