package org.bazar.adapter.s3;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bazar.app.api.ConfigProvider;
import org.bazar.app.api.GetStorageUrlsOutbound;
import org.bazar.domain.File;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@ApplicationScoped
public class GetStorageUrlsAdapter implements GetStorageUrlsOutbound {
    private final S3Presigner s3Presigner;
    private final ConfigProvider configProvider;

    @Inject
    public GetStorageUrlsAdapter(S3Presigner s3Presigner, ConfigProvider configProvider) {
        this.s3Presigner = s3Presigner;
        this.configProvider = configProvider;
    }

    @Override
    public String getUploadUrl(File file) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(configProvider.getFilesBucketName())
                .key(file.getObjectKey())
                .contentType(file.getContentType())
                .build();
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(objectRequest)
                .signatureDuration(Duration.ofSeconds(configProvider.getUploadUrlTtl()))
                .build();
        return s3Presigner.presignPutObject(presignRequest).url().toString();
    }

    @Override
    public String getDownloadUrl(File file) {
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(configProvider.getFilesBucketName())
                .key(file.getObjectKey())
                .responseContentDisposition("attachment; filename=\"" + file.getFileName() + "\"")
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(objectRequest)
                .signatureDuration(Duration.ofSeconds(configProvider.getDownloadUrlTtl()))
                .build();
        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
