package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementProjectFundingReq;
import eco.ywhc.xr.common.model.dto.res.FrameworkAgreementProjectFundingRes;
import eco.ywhc.xr.common.model.entity.FrameworkAgreementProjectFunding;
import org.mapstruct.Mapper;

@Mapper
public interface FrameworkAgreementProjectFundingConverter
        extends SimpleConverter<FrameworkAgreementProjectFunding, FrameworkAgreementProjectFundingReq, FrameworkAgreementProjectFundingRes> {

}
