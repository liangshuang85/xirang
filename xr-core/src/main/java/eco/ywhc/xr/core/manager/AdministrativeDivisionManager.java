package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.res.AdministrativeDivisionRes;
import eco.ywhc.xr.common.model.entity.AdministrativeDivision;
import eco.ywhc.xr.common.model.query.AdministrativeDivisionQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 行政区划管理
 */
@Transactional(rollbackFor = {Exception.class})
public interface AdministrativeDivisionManager {

    /**
     * 获取所有行政区
     */
    List<AdministrativeDivision> findAllEntities();

    /**
     * 获取所有行政区
     */
    List<AdministrativeDivisionRes> findAll();

    /**
     * 获取所有符合条件的行政区
     */
    List<AdministrativeDivisionRes> findMany(AdministrativeDivisionQuery query);

    /**
     * 获取指定行政区
     *
     * @param adcode 行政区划代码
     */
    AdministrativeDivisionRes findByAdcode(long adcode);

}
