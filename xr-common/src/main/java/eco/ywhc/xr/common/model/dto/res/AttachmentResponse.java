package eco.ywhc.xr.common.model.dto.res;

import eco.ywhc.xr.common.constant.FileOwnerType;
import eco.ywhc.xr.common.constant.FileType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.sugar.crud.model.IdentifiableResponse;

import java.time.OffsetDateTime;

/**
 * 附件
 */
@Getter
@Setter
@ToString
public class AttachmentResponse implements IdentifiableResponse<Long> {

    /**
     * ID
     */
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
     * 创建时间
     */
    private OffsetDateTime createdAt;

}
