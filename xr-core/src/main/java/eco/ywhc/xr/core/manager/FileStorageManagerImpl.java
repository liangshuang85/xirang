package eco.ywhc.xr.core.manager;

import eco.ywhc.xr.common.model.UploadedFile;
import eco.ywhc.xr.common.util.ChecksumUtils;
import eco.ywhc.xr.core.config.property.FileStorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.sugar.commons.exception.InternalErrorException;
import org.sugar.commons.exception.InvalidFileException;
import org.sugar.commons.exception.StorageException;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.utils.Md5Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;


/**
 * 存储
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageManagerImpl implements FileStorageManager {

    private final FileStorageProperties fileStorageProperties;

    private final @Nullable S3Client s3Client;

    @Override
    public UploadedFile prepare(MultipartFile multipartFile) {
        // 检查文件Content-Type
        if (StringUtils.isEmpty(multipartFile.getContentType())) {
            throw new InvalidFileException("文件缺少Content-Type");
        }
        try {
            String contentSha256 = ChecksumUtils.sha256Hex(multipartFile.getBytes());
            return new UploadedFile(multipartFile, contentSha256);
        } catch (IOException ex) {
            throw new StorageException("文件存储失败");
        }
    }

    @Override
    public UploadedFile prepare(InputStream inputStream) {
        try {
            return new UploadedFile(inputStream);
        } catch (IOException e) {
            throw new StorageException("文件存储失败");
        }
    }

    @Override
    public void store(UploadedFile uploadedFile) {
        if (fileStorageProperties.getLocal().isEnabled()) {
            saveToLocal(uploadedFile);
        }
        if (fileStorageProperties.getS3().isEnabled()) {
            saveToS3(uploadedFile);
        }
        Assert.hasText(uploadedFile.getFileUrl(), "文件存储失败");
    }

    /**
     * UploadedFile转byte[]
     */
    private byte[] convertMultipartFileToBytes(UploadedFile uploadedFile) {
        try {
            return uploadedFile.getBytes();
        } catch (IOException ex) {
            log.warn("转换UploadedFile为byte[]失败");
            throw new StorageException("转换MUploadedFile为byte[]失败");
        }
    }

    /**
     * 存储文件时不使用原始文件名，而是生成一个后缀与原始文件相同的新的文件名
     */
    private Path generateFilePath(String originalFilename, String contentSha256) {
        return Paths.get(new SimpleDateFormat("yyyy/MM").format(new Date()),
                contentSha256.substring(0, 2),
                contentSha256.substring(2, 12) + "." + FilenameUtils.getExtension(originalFilename));
    }

    /**
     * 保存文件到本地路径
     */
    private void saveToLocal(UploadedFile uploadedFile) {
        log.debug("保存文件到本地路径");
        byte[] bytes = convertMultipartFileToBytes(uploadedFile);
        // 生成文件存储路径
        Path localFilePath = generateFilePath(uploadedFile.getFileName(), uploadedFile.getSha256sum());
        Path target = Paths.get(fileStorageProperties.getLocal().getBasePath(), localFilePath.toString())
                .toAbsolutePath().normalize();
        try {
            log.trace("文件存储路径: {}", target);
            if (!Files.exists(target.getParent())) {
                Files.createDirectories(target.getParent());
            }
            Files.write(target, bytes);
        } catch (IOException ex) {
            log.warn("保存文件到数据目录失败");
            throw new StorageException("保存文件失败");
        }
        String unixFilePath = FilenameUtils.separatorsToUnix(localFilePath.toString());
        uploadedFile.setFilePath(unixFilePath);
        String fileUrl = getLocalUrl(fileStorageProperties.getLocal().getBaseUrl(), unixFilePath);
        uploadedFile.setFileUrl(fileUrl);
    }

    /**
     * 保存文件到S3对象存储
     */
    private void saveToS3(UploadedFile uploadedFile) {
        log.debug("保存文件到S3对象存储");
        if (s3Client == null) {
            log.error("s3Client未加载");
            throw new StorageException("S3对象存储未正确配置");
        }
        byte[] bytes = convertMultipartFileToBytes(uploadedFile);
        // 计算文件哈希值
        String contentMd5B64 = Md5Utils.md5AsBase64(bytes);
        // 生成文件存储路径
        Path localFilePath = generateFilePath(uploadedFile.getFileName(), uploadedFile.getSha256sum());
        String unixFilePath = FilenameUtils.separatorsToUnix(localFilePath.toString());
        String folder = prepareS3Folder();
        String key = FilenameUtils.separatorsToUnix(Paths.get(folder, unixFilePath).toString());
        String bucket = fileStorageProperties.getS3().getBucket();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(MediaType.valueOf(Objects.requireNonNull(uploadedFile.getMimeType())).toString())
                .contentLength(uploadedFile.getFileSize())
                .contentMD5(contentMd5B64)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
        } catch (AwsServiceException | SdkClientException ex) {
            log.error("上传文件到S3失败：{}", ex.getMessage());
            throw new StorageException("上传文件到S3失败");
        }
        uploadedFile.setFilePath(key);
        uploadedFile.setFileUrl(getS3Url(bucket, key));
    }

    private String getLocalUrl(String baseUrl, String filePath) {
        try {
            URL url = new URL(new URL(baseUrl), filePath);
            return url.toString();
        } catch (MalformedURLException e) {
            log.error("vendor.file-storage.local.basePath配置错误");
            throw new InternalErrorException(e);
        }
    }

    private String prepareS3Folder() {
        String folder = Optional.ofNullable(fileStorageProperties.getS3().getFolder()).orElse("");
        return folder.trim().replaceFirst("^/+", "");
    }

    private String getS3Url(String bucket, String key) {
        if (s3Client == null) {
            log.error("s3Client未加载");
            throw new StorageException("S3对象存储未正确配置");
        }
        GetUrlRequest request = GetUrlRequest.builder().bucket(bucket).key(key).build();
        URL url = s3Client.utilities().getUrl(request);
        return url.toString();
    }

}
