package eco.ywhc.xr.common.event;

import eco.ywhc.xr.common.constant.TaskTemplateRefType;
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
     * 任务关联对象ID
     */
    private final Long id;

    /**
     * 任务关联对象名
     */
    private final String name;

    /**
     * 任务关联对象类型
     */
    private final TaskTemplateRefType refType;

    /**
     * 当前要添加的成员
     */
    private final List<String> currentMembers;

}
