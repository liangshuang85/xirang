package eco.ywhc.xr.core.event;

import eco.ywhc.xr.common.constant.ApprovalStatusType;
import eco.ywhc.xr.common.constant.ApprovalTemplateRefType;
import eco.ywhc.xr.common.constant.ApprovalType;
import eco.ywhc.xr.common.converter.ApprovalConverter;
import eco.ywhc.xr.common.event.ClueCreatedEvent;
import eco.ywhc.xr.common.model.entity.Approval;
import eco.ywhc.xr.core.manager.ApprovalTemplateManager;
import eco.ywhc.xr.core.mapper.ApprovalMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

/**
 * 线索相关的事件
 */
@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class})
public class ClueEventListener {

    private final ApprovalMapper approvalMapper;

    private final ApprovalTemplateManager approvalTemplateManager;

    private final ApprovalConverter approvalConverter;

    @TransactionalEventListener
    public void onApplicationEvent(ClueCreatedEvent event) {
        log.debug("处理线索已创建事件：{}", event);
        createApprovals(event.getClue().getId(), ApprovalType.CLUE_APPROVAL);
    }

    public void createApprovals(long id, ApprovalType type) {
        List<Approval> approvals = approvalTemplateManager.listByType(ApprovalTemplateRefType.CLUE, type).stream()
                .map(i -> {
                    Approval approval = approvalConverter.fromApprovalTemplate(i);
                    approval.setDepartmentName(i.getDepartment());
                    approval.setRefId(id);
                    approval.setApprovalStatus(ApprovalStatusType.PENDING_START);
                    return approval;
                })
                .toList();
        if (CollectionUtils.isEmpty(approvals)) {
            return;
        }
        approvalMapper.bulkInsert(approvals);
    }

}
