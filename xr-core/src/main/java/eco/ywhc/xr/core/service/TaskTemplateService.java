package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.req.TaskTemplateReq;
import eco.ywhc.xr.common.model.dto.res.TaskTemplateRes;
import org.springframework.transaction.annotation.Transactional;
import org.sugar.crud.model.PageableModelSet;

/**
 * 任务模板
 */
@Transactional(rollbackFor = {Exception.class})
public interface TaskTemplateService {

    /**
     * 创建一个任务模板
     */
    Long createOne(TaskTemplateReq req);

    /**
     * 获取任务模板列表
     */
    PageableModelSet<TaskTemplateRes> findMany();

    /**
     * 获取一个任务模板
     *
     * @param id 任务模板ID
     */
    TaskTemplateRes findOne(long id);

    /**
     * 更新一个任务模板
     *
     * @param id 任务模板ID
     */
    int updateOne(long id, TaskTemplateReq req);

    /**
     * 删除一个任务模板
     *
     * @param id 任务模板ID
     */
    int deleteOne(long id);

}
