package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import eco.ywhc.xr.common.converter.ApprovalConverter;
import eco.ywhc.xr.common.model.dto.res.ApprovalRes;
import eco.ywhc.xr.common.model.entity.Approval;
import eco.ywhc.xr.core.mapper.ApprovalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审批记录(eco.ywhc.xr.common.model.entity.BApproval)表服务实现类
 *
 * @author makejava
 * @since 2024-04-18 11:51:08
 */
@Service
@RequiredArgsConstructor
public class ApprovalManagerImpl implements ApprovalManager {

    private final ApprovalMapper approvalMapper;

    private final ApprovalConverter approvalConverter;

    @Override
    public List<Approval> findAllEntitiesByClueId(long clueId) {
        QueryWrapper<Approval> qw = new QueryWrapper<>();
        qw.lambda().eq(Approval::getDeleted, 0)
                .eq(Approval::getRefId, clueId);
        return approvalMapper.selectList(qw);
    }

    @Override
    public List<ApprovalRes> findAllByClueId(long clueId) {
        return findAllEntitiesByClueId(clueId).stream().map(approvalConverter::toResponse).toList();
    }

    @Override
    public void logicDeleteAllEntitiesByClueId(long clueId) {
        UpdateWrapper<Approval> uw = new UpdateWrapper<>();
        uw.lambda().eq(Approval::getDeleted, false)
                .eq(Approval::getRefId, clueId)
                .set(Approval::getDeleted, true);
        approvalMapper.update(null, uw);
    }

}
