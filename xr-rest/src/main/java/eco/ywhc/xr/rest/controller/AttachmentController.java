package eco.ywhc.xr.rest.controller;

import eco.ywhc.xr.common.converter.AttachmentConverter;
import eco.ywhc.xr.common.model.UploadedFile;
import eco.ywhc.xr.common.model.dto.res.AttachmentResponse;
import eco.ywhc.xr.common.model.entity.Attachment;
import eco.ywhc.xr.core.manager.AttachmentManager;
import eco.ywhc.xr.core.manager.FileStorageManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 附件管理接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AttachmentController {

    private final FileStorageManager fileStorageManager;

    private final AttachmentManager attachmentManager;

    private final AttachmentConverter attachmentConverter;

    /**
     * 上传附件
     *
     * @param files MultipartFile列表
     */
    @PostMapping(value = "/attachments", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public List<AttachmentResponse> upload(@RequestParam("file") MultipartFile[] files) {
        List<AttachmentResponse> attachments = new ArrayList<>();
        Arrays.stream(files).forEach(multipartFile -> {
            UploadedFile uploadedFile = fileStorageManager.prepare(multipartFile);
            Attachment attachment = attachmentManager.findEntityByChecksum(uploadedFile.getChecksum());
            Attachment newAttachment;
            if (attachment == null) {
                fileStorageManager.store(uploadedFile);
                newAttachment = attachmentManager.save(uploadedFile);
            } else {
                log.info("已存在相同哈希值的文件");
                newAttachment = attachmentManager.copy(attachment);
                newAttachment.setFileName(multipartFile.getOriginalFilename());
                attachmentManager.createOne(newAttachment);
            }
            attachments.add(attachmentConverter.toResponse(newAttachment));
        });

        return attachments;
    }

}
