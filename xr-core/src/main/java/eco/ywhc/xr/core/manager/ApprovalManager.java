package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.res.ApprovalRes;
import eco.ywhc.xr.common.model.entity.Approval;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 审批记录(eco.ywhc.xr.common.model.entity.BApproval)表服务接口
 *
 * @author makejava
 * @since 2024-04-18 11:51:08
 */
@Transactional(rollbackFor = {Exception.class})
public interface ApprovalManager {

    /**
     * 根据线索Id查找审批
     *
     * @param clueId 线索Id
     */
    List<Approval> findAllEntitiesByClueId(long clueId);

    /**
     * 根据线索Id返回审批Res
     *
     * @param clueId 线索Id
     */
    List<ApprovalRes> findAllByClueId(long clueId);

    /**
     * 根据线索Id删除审批
     *
     * @param clueId 线索id
     */
    void logicDeleteAllEntitiesByClueId(long clueId);

}

