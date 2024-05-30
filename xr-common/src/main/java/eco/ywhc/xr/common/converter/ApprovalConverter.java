package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.res.ApprovalRes;
import eco.ywhc.xr.common.model.entity.Approval;
import eco.ywhc.xr.common.model.entity.ApprovalTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ApprovalConverter {

    @Mapping(target = "id", ignore = true)
    Approval fromApprovalTemplate(ApprovalTemplate approvalTemplate);

    ApprovalRes toResponse(Approval source);

}
