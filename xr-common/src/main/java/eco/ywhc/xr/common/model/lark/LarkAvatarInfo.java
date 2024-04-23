package eco.ywhc.xr.common.model.lark;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor(staticName = "of")
public class LarkAvatarInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = -338345591576577866L;

    /**
     * 72*72像素头像链接
     */
    private String avatar72;

    /**
     * 240*240像素头像链接
     */
    private String avatar240;

    /**
     * 640*640像素头像链接
     */
    private String avatar640;

    /**
     * 原始头像链接
     */
    private String avatarOrigin;

}