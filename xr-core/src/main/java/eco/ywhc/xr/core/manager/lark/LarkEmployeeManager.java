package eco.ywhc.xr.core.manager.lark;

import com.lark.oapi.service.ehr.v1.model.ListEmployeeRespBody;
import eco.ywhc.xr.common.model.lark.LarkEmployee;

import java.util.Set;

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

    /**
     * 保存员工的用户ID
     */
    void appendLarkEmployeeUserId(String... userId);

    /**
     * 获取全部员工的用户ID
     */
    Set<String> getAllLarkEmployeeUserIds();

}
