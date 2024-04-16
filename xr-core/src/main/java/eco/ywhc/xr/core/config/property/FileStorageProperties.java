package eco.ywhc.xr.core.config.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文件存储相关配置属性
 */
@Getter
@Setter
@ToString
@Configuration
@RequiredArgsConstructor
@Slf4j
@ConfigurationProperties(prefix = "vendor.file-storage")
public class FileStorageProperties {

    private LocalStorage local;

    private ObjectStorage s3;

    @Getter
    @Setter
    @ToString
    public static class LocalStorage {

        /**
         * 是否启用
         */
        private boolean enabled;

        /**
         * 存储路径
         */
        private String basePath;

        /**
         * 存储的URL
         */
        private String baseUrl;

    }

    @Getter
    @Setter
    @ToString
    public static class ObjectStorage {

        /**
         * 是否启用
         */
        private boolean enabled;

        /**
         * S3 Access Key
         */
        private String accessKey;

        /**
         * S3 Secret Key
         */
        private String secretKey;

        /**
         * S3终端节点
         */
        private String endpoint;

        /**
         * 存储桶
         */
        private String bucket;

        /**
         * 目录
         */
        private String folder;

        /**
         * 区域
         */
        private String region;

    }

}
