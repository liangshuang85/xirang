package eco.ywhc.xr.core.manager.lark;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lark.oapi.Client;
import com.lark.oapi.service.contact.v3.enums.GetDepartmentDepartmentIdTypeEnum;
import com.lark.oapi.service.contact.v3.enums.GetDepartmentUserIdTypeEnum;
import com.lark.oapi.service.contact.v3.model.*;
import eco.ywhc.xr.common.model.dto.res.DepartmentRes;
import eco.ywhc.xr.common.model.entity.LarkDepartment;
import eco.ywhc.xr.core.mapper.LarkDepartmentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.sugar.commons.exception.InternalErrorException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LarkDepartmentManagerImpl implements LarkDepartmentManager {

    private static final int BATCH_SIZE = 50;

    private final Client larkClient;

    private final LarkDepartmentMapper larkDepartmentMapper;

    @Override
    public Department getLarkDepartmentByDepartmentId(String departmentId) {
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
        return resp.getData().getDepartment();
    }

    @Override
    public DepartmentRes getDepartmentByDepartmentId(@NonNull String departmentId) {
        Department department = getLarkDepartmentByDepartmentId(departmentId);
        return DepartmentRes.of(department.getName(), department.getDepartmentId(), department.getLeaderUserId());
    }

    @Override
    public void syncLarkDepartments() {
        log.debug("开始同步飞书部门信息");
        boolean hasMore;
        String pageToken = null;
        List<String> currentDepartmentIds = listAllLarkDepartments().stream()
                .map(LarkDepartment::getDepartmentId)
                .collect(Collectors.toList());
        do {
            List<LarkDepartment> larkDepartments = listLarkDepartments(pageToken);
            if (larkDepartments.isEmpty()) {
                break;
            }
            for (LarkDepartment larkDepartment : larkDepartments) {
                upsertLarkDepartment(larkDepartment);
                currentDepartmentIds.remove(larkDepartment.getDepartmentId());
            }
            hasMore = larkDepartments.size() == BATCH_SIZE;
            pageToken = larkDepartments.get(larkDepartments.size() - 1).getDepartmentId();
        } while (Boolean.TRUE.equals(hasMore) && pageToken != null);
        if (!currentDepartmentIds.isEmpty()) {
            larkDepartmentMapper.deleteBatchIds(currentDepartmentIds);
        }
        log.debug("部门信息同步完成");
    }

    @Override
    public List<LarkDepartment> listLarkDepartments(String pageToken) {
        log.debug("开始获取部门信息，BATCH_SIZE={}", BATCH_SIZE);
        if (StringUtils.isBlank(pageToken)) {
            pageToken = null;
        }
        ChildrenDepartmentReq req = ChildrenDepartmentReq.newBuilder()
                .departmentId("0")
                .userIdType("open_id")
                .departmentIdType("department_id")
                .fetchChild(true)
                .pageToken(pageToken)
                .pageSize(BATCH_SIZE)
                .build();
        ChildrenDepartmentResp resp;
        try {
            resp = larkClient.contact().department().children(req);
        } catch (Exception e) {
            log.error("获取部门信息失败：{}", e.getMessage());
            throw new InternalErrorException(e);
        }
        if (!resp.success()) {
            log.error("获取部门信息失败：{}", resp.getMsg());
            throw new InternalErrorException();
        }
        return Arrays.stream(resp.getData().getItems())
                .map(item -> {
                    LarkDepartment larkDepartment = new LarkDepartment();
                    larkDepartment.setDepartmentId(item.getDepartmentId());
                    larkDepartment.setParentDepartmentId(item.getParentDepartmentId());
                    larkDepartment.setName(item.getName());
                    larkDepartment.setLeaderUserId(item.getLeaderUserId());
                    return larkDepartment;
                })
                .toList();
    }

    /**
     * 更新或插入部门信息
     *
     * @param larkDepartment 部门信息
     */
    private void upsertLarkDepartment(LarkDepartment larkDepartment) {
        LarkDepartment current = findEntityByDepartmentId(larkDepartment.getDepartmentId());
        if (current == null) {
            larkDepartmentMapper.insert(larkDepartment);
        } else if (!current.equals(larkDepartment)) {
            larkDepartmentMapper.updateById(larkDepartment);
        }
    }

    /**
     * 获取当前所有的部门
     */
    private List<LarkDepartment> listAllLarkDepartments() {
        return larkDepartmentMapper.selectList(null);
    }

    /**
     * 根据部门ID查询部门信息
     *
     * @param departmentId 部门ID
     */
    private LarkDepartment findEntityByDepartmentId(String departmentId) {
        QueryWrapper<LarkDepartment> qw = new QueryWrapper<>();
        qw.lambda().eq(LarkDepartment::getDepartmentId, departmentId);
        return larkDepartmentMapper.selectOne(qw);
    }

}
