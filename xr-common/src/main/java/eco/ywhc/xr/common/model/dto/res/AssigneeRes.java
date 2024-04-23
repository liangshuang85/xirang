package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.model.lark.LarkAvatarInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 负责人
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssigneeRes {

    /**
     * 负责人的飞书OpenID
     */
    private String assigneeId;

    /**
     * 中文姓名
     */
    private String assigneeName;

    /**
     * 用户头像信息
     */
    private LarkAvatarInfo avatarInfo;

}
