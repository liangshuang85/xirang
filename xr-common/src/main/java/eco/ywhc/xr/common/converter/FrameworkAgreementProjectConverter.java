package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementProjectReq;
import eco.ywhc.xr.common.model.dto.res.FrameworkAgreementProjectRes;
import eco.ywhc.xr.common.model.entity.FrameworkAgreementProject;
import org.mapstruct.Mapper;

@Mapper
public interface FrameworkAgreementProjectConverter
        extends SimpleConverter<FrameworkAgreementProject, FrameworkAgreementProjectReq, FrameworkAgreementProjectRes> {

}
