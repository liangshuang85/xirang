package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.req.OxygenHydrogenUsageReq;
import eco.ywhc.xr.common.model.dto.res.OxygenHydrogenUsageRes;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = {Exception.class})
public interface OxygenHydrogenUsageManager {

    /**
     * 批量用户企业液氧/绿氢使用量
     *
     * @param req   包含氧氢使用记录信息的列表
     * @param refId 关联ID
     */
    void createMany(List<OxygenHydrogenUsageReq> req, long refId);

    /**
     * 根据关联ID查找多个用户企业液氧/绿氢使用量，不以分页返回
     *
     * @param refId 关联ID
     */
    List<OxygenHydrogenUsageRes> findManyByRefId(long refId);

    void compareAndUpdate(List<OxygenHydrogenUsageReq> req, long refId);

}
