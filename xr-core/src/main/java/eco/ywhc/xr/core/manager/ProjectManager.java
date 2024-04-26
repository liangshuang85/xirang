package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.req.ProjectReq;
import eco.ywhc.xr.common.model.dto.res.ProjectRes;
import eco.ywhc.xr.common.model.entity.Project;
import eco.ywhc.xr.common.model.entity.ProjectInformation;
import eco.ywhc.xr.common.model.query.ProjectQuery;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.manager.BaseManager;

@Transactional(rollbackFor = {Exception.class})
public interface ProjectManager extends BaseManager<Long, Project, ProjectReq, ProjectRes, ProjectQuery> {

    @Override
    Project findEntityById(@NonNull Long id);

    /**
     * 根据项目ID获取项目信息
     *
     * @param id 项目ID
     */
    ProjectInformation getProjectInformationByProjectId(long id);

    /**
     * 关联附件
     * @param req 项目REQ
     * @param id 项目ID
     */
    void linkAttachments(ProjectReq req, long id);


}
