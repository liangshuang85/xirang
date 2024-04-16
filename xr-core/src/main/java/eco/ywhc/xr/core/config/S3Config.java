package eco.ywhc.xr.core.config;

import eco.ywhc.xr.core.config.property.FileStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final FileStorageProperties fileStorageProperties;

    @ConditionalOnExpression("${vendor.file-storage.s3.enabled:false}")
    @Bean(destroyMethod = "close")
    public S3Client s3Client() {
        FileStorageProperties.ObjectStorage objectStorage = fileStorageProperties.getS3();
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(objectStorage.getAccessKey(),
                objectStorage.getSecretKey());
        URI endpointUri = URI.create(objectStorage.getEndpoint());
        Region region = Region.of(fileStorageProperties.getS3().getRegion());
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .endpointOverride(endpointUri)
                .region(region)
                .build();
    }

}
