package eco.ywhc.xr.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import eco.ywhc.xr.common.constant.ApprovalTemplateRefType;
import eco.ywhc.xr.common.constant.ApprovalType;
import eco.ywhc.xr.common.model.entity.ApprovalTemplate;
import eco.ywhc.xr.core.mapper.ApprovalTemplateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalTemplateManagerImpl implements ApprovalTemplateManager {

    private final ApprovalTemplateMapper approvalTemplateMapper;

    @Override
    public List<ApprovalTemplate> listByType(@NonNull ApprovalTemplateRefType refType, @NonNull ApprovalType type) {
        QueryWrapper<ApprovalTemplate> qw = new QueryWrapper<>();
        qw.lambda().eq(ApprovalTemplate::getDeleted, 0)
                .eq(ApprovalTemplate::getRefType, refType)
                .eq(ApprovalTemplate::getType, type);
        return approvalTemplateMapper.selectList(qw);
    }

}
