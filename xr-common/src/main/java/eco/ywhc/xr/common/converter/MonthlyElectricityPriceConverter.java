package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.MonthlyElectricityPriceReq;
import eco.ywhc.xr.common.model.dto.res.MonthlyElectricityPriceRes;
import eco.ywhc.xr.common.model.entity.MonthlyElectricityPrice;
import org.mapstruct.Mapper;

@Mapper
public interface MonthlyElectricityPriceConverter extends SimpleConverter<MonthlyElectricityPrice, MonthlyElectricityPriceReq, MonthlyElectricityPriceRes> {

}
