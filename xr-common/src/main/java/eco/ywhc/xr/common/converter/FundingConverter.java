package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.FundingReq;
import eco.ywhc.xr.common.model.dto.res.FundingRes;
import eco.ywhc.xr.common.model.entity.Funding;
import org.mapstruct.Mapper;

@Mapper
public interface FundingConverter extends SimpleConverter<Funding, FundingReq, FundingRes> {

}
