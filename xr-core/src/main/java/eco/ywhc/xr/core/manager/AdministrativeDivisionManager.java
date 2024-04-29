package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.dto.res.AdministrativeDivisionRes;
import eco.ywhc.xr.common.model.entity.AdministrativeDivision;
import eco.ywhc.xr.common.model.query.AdministrativeDivisionQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 行政区划管理
 */
@Transactional(rollbackFor = {Exception.class})
public interface AdministrativeDivisionManager {

    /**
     * 获取所有行政区
     */
    List<AdministrativeDivision> findAllEntitiesByAdcodes(Collection<Long> adcodes);

    /**
     * 获取所有行政区
     */
    List<AdministrativeDivision> findAllEntities();

    /**
     * 获取所有行政区
     */
    List<AdministrativeDivisionRes> findAll();

    /**
     * 获取所有行政区
     *
     * @return Key为 {@link AdministrativeDivision#adcode 行政区划代码}，Value为 {@link AdministrativeDivision 行政区}
     */
    Map<Long, AdministrativeDivision> findAllEntitiesAsMap();

    /**
     * 获取所有行政区
     *
     * @return Key为 {@link AdministrativeDivision#adcode 行政区划代码}，Value为 {@link AdministrativeDivisionRes 行政区}
     */
    Map<Long, AdministrativeDivisionRes> findAllAsMap();

    /**
     * 获取所有符合条件的行政区
     */
    List<AdministrativeDivisionRes> findMany(AdministrativeDivisionQuery query);

    /**
     * 获取指定行政区
     *
     * @param adcode 行政区划代码
     */
    AdministrativeDivision findEntityByAdcode(long adcode);

    /**
     * 获取指定行政区
     *
     * @param adcode 行政区划代码
     */
    AdministrativeDivisionRes findByAdcode(long adcode);

    /**
     * 获取指定行政区。需要确保{@link  AdministrativeDivisionRes#adcode 行政区划代码}对应的行政区肯定存在
     *
     * @param adcode 行政区划代码
     */
    AdministrativeDivisionRes findByAdcodeSurely(long adcode);

    /**
     * 获取指定行政区。需要确保{@link  AdministrativeDivisionRes#adcode 行政区划代码}对应的行政区肯定存在
     *
     * @param adcodes 行政区划代码列表
     */
    List<AdministrativeDivisionRes> findAllByAdcodesSurely(Collection<Long> adcodes);

    /**
     * 获取指定行政区。需要确保{@link  AdministrativeDivisionRes#adcode 行政区划代码}对应的行政区肯定存在
     *
     * @param adcodes 行政区划代码列表
     * @return Key为行政区划代码，Value为对应行政区
     */
    Map<Long, AdministrativeDivisionRes> findAllAsMapByAdcodesSurely(Collection<Long> adcodes);

    /**
     * 分析指定行政区，返回其上级全部行政区
     *
     * @param adcode 行政区划代码
     * @return Key为上级行政区的 {@link AdministrativeDivision#level 级别}，Value为 {@link AdministrativeDivision 行政区}
     */
    Map<Short, AdministrativeDivision> analyzeByAdcode(long adcode);

    /**
     * 查找从指定行政区开始的全部直接和间接下级行政区
     *
     * @param firstAdcode 第一个行政区划代码
     */
    List<AdministrativeDivision> findAllEntitiesSince(long firstAdcode);

    /**
     * 查找从指定行政区开始的全部直接和间接下级行政区，返回一个行政区划代码列表
     *
     * @param firstAdcode 第一个行政区划代码
     */
    List<Long> findAllEntityIdsSince(long firstAdcode);

}
