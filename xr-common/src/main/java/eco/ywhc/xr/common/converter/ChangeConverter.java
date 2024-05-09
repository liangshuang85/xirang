package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.res.ChangeRes;
import eco.ywhc.xr.common.model.entity.Change;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ChangeConverter {

    List<ChangeRes> toResponseList(List<Change> changeList);

}
