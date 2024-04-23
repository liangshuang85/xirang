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

    List<Approval> findAllEntitiesByClueId(long clueId);

    List<ApprovalRes> findAllByClueId(long clueId);

    void logicDeleteAllEntitiesByClueId(long clueId);

}

