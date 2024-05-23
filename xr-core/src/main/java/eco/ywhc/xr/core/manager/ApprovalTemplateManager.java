package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.constant.ApprovalTemplateRefType;
import eco.ywhc.xr.common.constant.ApprovalType;
import eco.ywhc.xr.common.model.entity.ApprovalTemplate;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ApprovalTemplateManager {

    /**
     * 根据类型获取审批模板列表
     */
    List<ApprovalTemplate> listByType(@NonNull ApprovalTemplateRefType refType, @NonNull ApprovalType type);

    /**
     * 查找指定审批模板
     *
     * @param id 审批模板ID
     */
    ApprovalTemplate findEntityById(long id);

    /**
     * 查找指定审批模板，没有找到则抛出异常
     *
     * @param id 审批模板ID
     */
    ApprovalTemplate mustFindEntityById(long id);

}
