package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.constant.KeypairGeneratorAlgorithm;
import eco.ywhc.xr.common.model.PemKeyPair;
import eco.ywhc.xr.common.model.dto.req.ApplicationReq;
import eco.ywhc.xr.common.model.dto.res.ApplicationRes;
import eco.ywhc.xr.common.model.dto.res.PermissionRes;
import eco.ywhc.xr.common.model.query.ApplicationQuery;
import org.sugar.crud.model.PageableModelSet;

import java.util.Collection;
import java.util.List;

/**
 * 应用
 */
public interface ApplicationService {

    /**
     * 创建一个应用
     */
    Long createOne(ApplicationReq req);

    /**
     * 获取应用列表
     */
    PageableModelSet<ApplicationRes> findMany(ApplicationQuery query);

    /**
     * 获取指定应用
     */
    ApplicationRes findOne(long id);

    /**
     * 更新一个应用
     */
    int updateOne(long id, ApplicationReq req);

    /**
     * 删除一个应用
     */
    int deleteOne(long id, boolean logical);

    /**
     * 启用一个应用
     *
     * @param id 应用ID
     */
    void enable(long id);

    /**
     * 禁用一个应用
     *
     * @param id 应用ID
     */
    void disable(long id);

    /**
     * 设置应用的权限
     *
     * @param id              应用ID
     * @param permissionCodes 权限编码集合
     */
    void configurePermissions(long id, Collection<String> permissionCodes);

    /**
     * 获取指定应用的权限
     *
     * @param id 应用ID
     */
    List<PermissionRes> listPermissions(long id);

    /**
     * 生成PEM格式的密钥对
     *
     * @param algorithm 密钥对生成算法
     */
    PemKeyPair generatePemKeyPair(long id, KeypairGeneratorAlgorithm algorithm);

}
