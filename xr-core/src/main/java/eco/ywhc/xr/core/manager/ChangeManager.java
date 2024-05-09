package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.res.ChangeRes;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 变更记录
 */
@Transactional(rollbackFor = {Exception.class})
public interface ChangeManager {

    /**
     * 根据refId查询变更记录
     *
     * @param refId 关联ID
     */
    List<ChangeRes> findAllByRefId(long refId);

    /**
     * 根据refId删除变更记录
     *
     * @param refId 关联ID
     */
    void bulkDeleteByRefId(long refId);

}
