package eco.ywhc.xr.common.event;

import eco.ywhc.xr.common.model.entity.Project;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 事件：项目已创建
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor(staticName = "of")
public class ProjectCreatedEvent {

    /**
     * 项目
     */
    private final Project project;

}
