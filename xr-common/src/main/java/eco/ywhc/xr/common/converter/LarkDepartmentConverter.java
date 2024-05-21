package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.dto.res.LarkDepartmentRes;
import eco.ywhc.xr.common.model.entity.LarkDepartment;
import org.mapstruct.Mapper;

/**
 * 部门
 */
@Mapper
public interface LarkDepartmentConverter {

    LarkDepartmentRes toResponse(LarkDepartment source);

}
