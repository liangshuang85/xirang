package eco.ywhc.xr.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import eco.ywhc.xr.common.constant.FileOwnerType;
import eco.ywhc.xr.common.constant.FileType;
import lombok.*;
import org.sugar.crud.entity.IdentifiableEntity;

import java.io.Serial;


/**
 * 附件
 */
@Getter
@Setter
@TableName(value = "b_attachment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment extends BaseEntity implements IdentifiableEntity<Long> {

    @Serial
    private static final long serialVersionUID = -3857958608593588707L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 属主类型
     */
    private FileOwnerType ownerType;

    /**
     * 属主ID
     */
    private Long ownerId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件大小，单位为字节
     */
    private Long fileSize;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 文件类型
     */
    private FileType fileType;

    /**
     * 文件MimeType
     */
    private String mimeType;

    /**
     * 文件Checksum
     */
    private String checksum;

}
