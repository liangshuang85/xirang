package eco.ywhc.xr.core.manager.lark;

import com.lark.oapi.Client;
import com.lark.oapi.service.contact.v3.enums.GetDepartmentDepartmentIdTypeEnum;
import com.lark.oapi.service.contact.v3.enums.GetDepartmentUserIdTypeEnum;
import com.lark.oapi.service.contact.v3.model.Department;
import com.lark.oapi.service.contact.v3.model.GetDepartmentReq;
import com.lark.oapi.service.contact.v3.model.GetDepartmentResp;
import eco.ywhc.xr.common.model.dto.res.DepartmentRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;

@Service
@RequiredArgsConstructor
@Slf4j
public class LarkDepartmentManagerImpl implements LarkDepartmentManager {

    private final Client larkClient;

    @Override
    public DepartmentRes getDepartmentByDepartmentId(@NonNull String departmentId) {
        GetDepartmentReq req = GetDepartmentReq.newBuilder()
                .departmentId(departmentId)
                .userIdType(GetDepartmentUserIdTypeEnum.OPEN_ID)
                .departmentIdType(GetDepartmentDepartmentIdTypeEnum.DEPARTMENT_ID)
                .build();
        GetDepartmentResp resp;
        try {
            resp = larkClient.contact().department().get(req);
        } catch (Exception e) {
            log.error("获取部门信息失败：{}", e.getMessage());
            throw new InternalErrorException(e);
        }
        if (!resp.success()) {
            log.error("获取部门信息失败：{}", resp.getMsg());
            throw new InternalErrorException();
        }
        Department department = resp.getData().getDepartment();
        return DepartmentRes.of(department.getName(), department.getDepartmentId(), department.getLeaderUserId());
    }

}
