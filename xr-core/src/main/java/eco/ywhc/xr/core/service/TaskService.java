package eco.ywhc.xr.core.service;

import eco.ywhc.xr.common.event.InstanceRoleLarkMemberInsertedEvent;

public interface TaskService {

    /**
     * 发起一个飞书任务
     *
     * @param id 任务ID
     */
    int startLarkTask(long id);

    void InstanceRoleLarkMemberInsertedEvent(InstanceRoleLarkMemberInsertedEvent event);

}
