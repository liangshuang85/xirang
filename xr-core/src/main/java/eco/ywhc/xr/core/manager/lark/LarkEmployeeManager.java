package eco.ywhc.xr.core.manager.lark;

import com.lark.oapi.service.ehr.v1.model.ListEmployeeRespBody;
import eco.ywhc.xr.common.model.lark.LarkEmployee;

/**
 * 飞书用户管理
 */
public interface LarkEmployeeManager {

    /**
     * 获取员工花名册信息
     *
     * @param pageToken 分页标记
     */
    ListEmployeeRespBody listEmployees(String pageToken);

    /**
     * 获取用户信息
     *
     * @param userId 用户OpenId
     */
    LarkEmployee retrieveLarkEmployee(String userId);

}
