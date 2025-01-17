package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.constant.ApprovalTemplateRefType;
import eco.ywhc.xr.common.model.ApprovalForm;
import eco.ywhc.xr.common.model.dto.res.ApprovalRes;
import eco.ywhc.xr.common.model.entity.Approval;
import org.springframework.lang.NonNull;
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
     * 查找指定审批
     *
     * @param id 审批ID
     */
    Approval findEntityById(long id);

    /**
     * 查找指定审批
     *
     * @param id 审批ID
     */
    Approval mustFoundEntityById(long id);

    /**
     * 获取飞书审批实例的待审批人
     *
     * @param instanceCode 审批实例Code
     */
    String getLarkApprovalMembers(String instanceCode);

    /**
     * 根据关联对象Id删除审批
     *
     * @param refId 关联对象id
     */
    void logicDeleteAllEntitiesByRefId(long refId);

    /**
     * 通过关联ID查询审批记录
     *
     * @param id 关联ID
     */
    List<ApprovalRes> listApprovalsByRefId(long id);

    /**
     * 从飞书接口更新审批状态
     *
     * @param approval 审批
     */
    void updateApprovalFromLark(Approval approval);

    /**
     * 获取飞书审批定义控件信息
     */
    List<ApprovalForm> getLarkApprovalForm(Approval approval);

    /**
     * 通过关联类型和关联ID查询关联的行政区划代码
     *
     * @param refType 关联类型
     * @param refId   关联ID
     */
    Long getAdcodeByRefTypeAndRefId(@NonNull ApprovalTemplateRefType refType, long refId);

    /**
     * 通过关联类型和关联ID查找对应负责人
     *
     * @param refType 关联类型
     * @param refId   关联ID
     */
    String getOpenIdByRefTypeAndRefId(ApprovalTemplateRefType refType, long refId);

    /**
     * 获取审核中的审批列表
     *
     * @param prevApprovalId 上一个审批ID
     * @param limit          限制数量
     */
    List<Approval> listApprovals(long prevApprovalId, int limit);

}

