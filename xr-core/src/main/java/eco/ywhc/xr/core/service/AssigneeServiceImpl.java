package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.model.dto.res.AssigneeRes;
import eco.ywhc.xr.common.model.lark.LarkEmployee;
import eco.ywhc.xr.core.manager.lark.LarkEmployeeManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.sugar.crud.model.PageableModelSet;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssigneeServiceImpl implements AssigneeService {

    private final LarkEmployeeManager larkEmployeeManager;

    @Override
    public PageableModelSet<AssigneeRes> findAll() {
        Set<String> userIds = larkEmployeeManager.getAllLarkEmployeeUserIds();
        List<AssigneeRes> assignees = userIds.stream()
                .map(i -> {
                    LarkEmployee larkEmployee = larkEmployeeManager.retrieveLarkEmployee(i);
                    if (larkEmployee == null) {
                        return null;
                    }
                    return AssigneeRes.builder()
                            .assigneeId(i)
                            .assigneeName(larkEmployee.getName())
                            .avatarInfo(larkEmployee.getAvatarInfo())
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();
        return PageableModelSet.of(assignees);
    }

}
