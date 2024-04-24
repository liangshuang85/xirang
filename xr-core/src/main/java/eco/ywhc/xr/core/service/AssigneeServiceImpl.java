package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.res.AssigneeRes;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AssigneeServiceImpl implements AssigneeService {

    private final LarkEmployeeManager larkEmployeeManager;

    @Override
    public PageableModelSet<AssigneeRes> findAll() {
        Set<String> userIds = larkEmployeeManager.getAllLarkEmployeeUserIds();
        List<AssigneeRes> assignees = userIds.stream()
                .map(i -> {
                    LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployee(i);
                    return AssigneeRes.builder()
                            .assigneeId(i)
                            .assigneeName(larkEmployee.getName())
                            .avatarInfo(larkEmployee.getAvatarInfo())
                            .build();
                })
                .toList();
        return PageableModelSet.of(assignees);
    }

}
