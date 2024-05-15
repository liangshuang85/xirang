package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.req.BasicDataReq;
import eco.ywhc.xr.common.model.dto.res.BasicDataRes;
import org.springframework.transaction.annotation.Transactional;

/**
 * 基础信息
 */
@Transactional(rollbackFor = {Exception.class})
public interface BasicDataManager {

    /**
     * 新建一个基础信息
     *
     * @param req   请求
     * @param refId 关联ID
     */
    void createOne(BasicDataReq req, long refId);

    /**
     * 更新一个基础信息
     *
     * @param req   请求
     * @param refId 关联ID
     */
    void updateOne(BasicDataReq req, long refId);

    /**
     * 获取基础信息
     *
     * @param refId 关联ID
     */
    BasicDataRes getBasicData(long refId);

    /**
     * 根据refId删除基础信息
     *
     * @param refId 关联ID
     */
    void deleteEntityByRefId(long refId);

}
