package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.VisitReq;
import eco.ywhc.xr.common.model.dto.res.VisitRes;
import eco.ywhc.xr.common.model.entity.Visit;
import org.mapstruct.Mapper;

@Mapper
public interface VisitConverter extends SimpleConverter<Visit, VisitReq, VisitRes>{

}
