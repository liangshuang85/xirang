package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.ElectricityLoadReq;
import eco.ywhc.xr.common.model.dto.res.ElectricityLoadRes;
import eco.ywhc.xr.common.model.entity.ElectricityLoad;
import org.mapstruct.Mapper;

@Mapper
public interface ElectricityLoadConverter extends SimpleConverter<ElectricityLoad, ElectricityLoadReq, ElectricityLoadRes> {

}
