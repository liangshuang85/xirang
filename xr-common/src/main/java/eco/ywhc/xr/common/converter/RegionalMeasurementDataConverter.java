package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.RegionalMeasurementDataReq;
import eco.ywhc.xr.common.model.dto.res.RegionalMeasurementDataRes;
import eco.ywhc.xr.common.model.entity.RegionalMeasurementData;
import org.mapstruct.Mapper;

@Mapper
public interface RegionalMeasurementDataConverter extends SimpleConverter<RegionalMeasurementData, RegionalMeasurementDataReq, RegionalMeasurementDataRes> {

}
