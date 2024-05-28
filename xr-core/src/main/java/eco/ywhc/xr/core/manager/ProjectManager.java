package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.req.ProjectInformationReq;
import eco.ywhc.xr.common.model.dto.req.ProjectReq;
import eco.ywhc.xr.common.model.dto.res.ProjectRes;
import eco.ywhc.xr.common.model.entity.Project;
import eco.ywhc.xr.common.model.entity.ProjectInformation;
import eco.ywhc.xr.common.model.query.ProjectQuery;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.manager.BaseManager;

/**
 * 项目
 */
@Transactional(rollbackFor = {Exception.class})
public interface ProjectManager extends BaseManager<Long, Project, ProjectReq, ProjectRes, ProjectQuery> {

    @Override
    Project findEntityById(@NonNull Long id);

    /**
     * 创建项目信息
     *
     * @param req 项目信息REQ
     * @param id  项目ID
     */
    void createProjectInformation(ProjectInformationReq req, long id);

    /**
     * 更新项目信息
     *
     * @param req 项目信息REQ
     * @param id  项目ID
     */
    void updateProjectInformation(ProjectInformationReq req, long id);

    /**
     * 逻辑删除项目信息
     *
     * @param id 项目ID
     */
    void logicDeleteProjectInformation(long id);

    /**
     * 根据项目ID获取项目信息
     *
     * @param id 项目ID
     */
    ProjectInformation getProjectInformationByProjectId(long id);

    /**
     * 关联附件
     *
     * @param req 项目REQ
     * @param id  项目ID
     */
    void compareAndUpdateAttachments(ProjectReq req, long id);

    /**
     * 设置附件
     *
     * @param res 项目RES
     */
    void findAndSetAttachments(ProjectRes res);

    /**
     * 根据关联框架协议ID查询是否存在项目
     *
     * @param frameworkAgreementId 关联框架协议ID
     */
    boolean isExistByFrameworkAgreementId(long frameworkAgreementId);

}
