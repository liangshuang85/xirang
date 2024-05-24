package eco.ywhc.xr.common.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 事件：实例角色成员已插入
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor(staticName = "of")
public class InstanceRoleLarkMemberInsertedEvent {

    /**
     * 当前要添加的成员
     */
    private final List<String> currentMembers;

    /**
     * 任务清单Guid
     */
    private final String taskListGuid;

}
