package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.ChannelEntryReq;
import eco.ywhc.xr.common.model.dto.res.ChannelEntryRes;
import eco.ywhc.xr.common.model.entity.ChannelEntry;
import org.mapstruct.Mapper;

@Mapper
public interface ChannelEntryConverter extends SimpleConverter<ChannelEntry, ChannelEntryReq, ChannelEntryRes> {

}
