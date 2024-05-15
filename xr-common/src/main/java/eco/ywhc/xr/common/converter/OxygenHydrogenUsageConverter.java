package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.OxygenHydrogenUsageReq;
import eco.ywhc.xr.common.model.dto.res.OxygenHydrogenUsageRes;
import eco.ywhc.xr.common.model.entity.OxygenHydrogenUsage;
import org.mapstruct.Mapper;

@Mapper
public interface OxygenHydrogenUsageConverter extends SimpleConverter<OxygenHydrogenUsage, OxygenHydrogenUsageReq, OxygenHydrogenUsageRes> {

}
