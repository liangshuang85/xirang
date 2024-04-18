package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementReq;
import eco.ywhc.xr.common.model.dto.res.FrameworkAgreementRes;
import eco.ywhc.xr.common.model.entity.FrameworkAgreement;
import org.mapstruct.Mapper;

@Mapper
public interface FrameworkAgreementConverter
        extends SimpleConverter<FrameworkAgreement, FrameworkAgreementReq, FrameworkAgreementRes> {

}
