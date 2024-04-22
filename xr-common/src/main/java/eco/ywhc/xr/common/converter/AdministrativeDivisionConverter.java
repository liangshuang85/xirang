package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.res.AdministrativeDivisionRes;
import eco.ywhc.xr.common.model.entity.AdministrativeDivision;
import org.mapstruct.Mapper;

/**
 * 行政区划对象转换器
 */
@Mapper
public interface AdministrativeDivisionConverter {

    AdministrativeDivisionRes toResponse(AdministrativeDivision source);

}
