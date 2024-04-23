package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.req.ClueReq;
import eco.ywhc.xr.common.model.dto.res.ClueRes;
import eco.ywhc.xr.common.model.entity.Clue;
import org.mapstruct.Mapper;

@Mapper
public interface ClueConverter extends SimpleConverter<Clue, ClueReq, ClueRes>{

}
