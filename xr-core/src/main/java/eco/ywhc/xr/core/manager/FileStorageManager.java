package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.UploadedFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 存储
 */
@Transactional(rollbackFor = {Exception.class})
public interface FileStorageManager {

    /**
     * 准备存储一个文件
     *
     * @param multipartFile Multipart文件
     */
    UploadedFile prepare(MultipartFile multipartFile);

    /**
     * 准备存储一个文件
     *
     * @param inputStream 输入流
     */
    UploadedFile prepare(InputStream inputStream);

    /**
     * 存储一个文件
     *
     * @param uploadedFile 上传的文件
     */
    void store(UploadedFile uploadedFile);

}
