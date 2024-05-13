package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.res.BasicDataRes;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基础信息
 */
@Transactional(rollbackFor = {Exception.class})
public interface BasicDataManager {

    /**
     * 根据refId查找基础信息
     *
     * @param refId 关联ID
     */
    BasicDataRes findEntityByRefId(long refId);

    /**
     * 根据refId删除基础信息
     *
     * @param refId 关联ID
     */
    void deleteEntityByRefId(long refId);

}
