package eco.ywhc.xr.common.model;


import eco.ywhc.xr.common.util.ChecksumUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 上传的文件
 */
@Getter
@Setter
@ToString
public final class UploadedFile {

    /**
     * 文件内容
     */
    private final byte[] content;

    /**
     * 文件SHA256哈希值
     */
    private final String sha256sum;

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
    private String fileType;

    /**
     * 文件MimeType
     */
    private String mimeType;

    /**
     * 文件Checksum，即文件SHA256哈希值
     */
    private String checksum;

    /**
     * @param inputStream 输入流对象
     */
    public UploadedFile(InputStream inputStream) throws IOException {
        this.content = FileCopyUtils.copyToByteArray(inputStream);
        this.sha256sum = ChecksumUtils.sha256Hex(content);
        this.fileSize = (long) content.length;
        this.checksum = sha256sum;
    }

    /**
     * @param multipartFile Multipart文件
     */
    public UploadedFile(MultipartFile multipartFile) throws IOException {
        this.content = multipartFile.getBytes();
        this.sha256sum = ChecksumUtils.sha256Hex(content);
        this.fileName = multipartFile.getOriginalFilename();
        this.fileSize = multipartFile.getSize();
        this.mimeType = multipartFile.getContentType();
        this.checksum = sha256sum;
    }

    /**
     * @param multipartFile Multipart文件
     * @param sha256sum     文件SHA256哈希值
     */
    public UploadedFile(MultipartFile multipartFile, String sha256sum) throws IOException {
        this.content = multipartFile.getBytes();
        this.sha256sum = sha256sum;
        this.fileName = multipartFile.getOriginalFilename();
        this.fileSize = multipartFile.getSize();
        this.mimeType = multipartFile.getContentType();
        this.checksum = sha256sum;
    }

    public byte[] getBytes() throws IOException {
        return content;
    }

    public String getSha256sum() {
        return sha256sum;
    }

}
