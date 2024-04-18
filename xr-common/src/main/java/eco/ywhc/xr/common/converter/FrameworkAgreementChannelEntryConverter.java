package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.FrameworkAgreementChannelEntryReq;
import eco.ywhc.xr.common.model.dto.res.FrameworkAgreementChannelEntryRes;
import eco.ywhc.xr.common.model.entity.FrameworkAgreementChannelEntry;
import org.mapstruct.Mapper;

@Mapper
public interface FrameworkAgreementChannelEntryConverter
        extends SimpleConverter<FrameworkAgreementChannelEntry, FrameworkAgreementChannelEntryReq, FrameworkAgreementChannelEntryRes> {

}
