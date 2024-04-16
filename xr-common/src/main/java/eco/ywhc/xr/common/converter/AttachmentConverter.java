package eco.ywhc.xr.common.converter;

import eco.ywhc.xr.common.model.UploadedFile;
import eco.ywhc.xr.common.model.dto.res.AttachmentResponse;
import eco.ywhc.xr.common.model.entity.Attachment;
import org.mapstruct.Mapper;

/**
 * 附件转换器
 */
@Mapper
public interface AttachmentConverter {

    AttachmentResponse toResponse(Attachment source);

    Attachment fromUploadedFile(UploadedFile source);

}
