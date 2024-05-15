package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.req.ElectricityLoadReq;
import eco.ywhc.xr.common.model.dto.res.ElectricityLoadRes;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = {Exception.class})
public interface ElectricityLoadManager {

    /**
     * 批量新增用电企业负荷相关信息
     *
     * @param electricityLoads 用电企业负荷相关信息列表
     * @param refId            关联ID
     */
    void createMany(List<ElectricityLoadReq> electricityLoads, long refId);

    /**
     * 根据关联ID查找多个用电企业负荷相关信息
     *
     * @param refId 关联ID
     */
    List<ElectricityLoadRes> findManyByRefId(long refId);

    /**
     * 比较并更新用电企业负荷相关信息
     *
     * @param req   用电企业负荷相关信息列表
     * @param refId 关联ID
     */
    void compareAndUpdate(List<ElectricityLoadReq> req, long refId);

}
