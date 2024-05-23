package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.ApprovalTemplateReq;
import eco.ywhc.xr.common.model.dto.res.ApprovalTemplateRes;
import eco.ywhc.xr.common.model.entity.ApprovalTemplate;
import org.mapstruct.Mapper;

@Mapper
public interface ApprovalTemplateConverter extends SimpleConverter<ApprovalTemplate, ApprovalTemplateReq, ApprovalTemplateRes> {

}
