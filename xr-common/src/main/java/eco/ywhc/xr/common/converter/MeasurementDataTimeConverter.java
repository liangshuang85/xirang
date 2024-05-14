package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.MeasurementDataTimeReq;
import eco.ywhc.xr.common.model.dto.res.MeasurementDataTimeRes;
import eco.ywhc.xr.common.model.entity.MeasurementDataTime;
import org.mapstruct.Mapper;

@Mapper
public interface MeasurementDataTimeConverter extends SimpleConverter<MeasurementDataTime, MeasurementDataTimeReq, MeasurementDataTimeRes> {

}
