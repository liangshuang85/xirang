package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.BasicDataReq;
import eco.ywhc.xr.common.model.dto.res.BasicDataRes;
import eco.ywhc.xr.common.model.entity.BasicData;
import org.mapstruct.Mapper;

@Mapper
public interface BasicDataConverter extends SimpleConverter<BasicData, BasicDataReq, BasicDataRes> {

}
