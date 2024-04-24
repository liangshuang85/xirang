package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.res.AssigneeRes;
import org.sugar.crud.model.PageableModelSet;

/**
 * 负责人
 */
public interface AssigneeService {

    /**
     * 获取全部负责人
     */
    PageableModelSet<AssigneeRes> findAll();

}
