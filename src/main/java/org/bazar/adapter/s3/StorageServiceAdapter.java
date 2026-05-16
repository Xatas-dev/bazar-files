package org.bazar.adapter.s3;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bazar.app.api.ConfigProvider;
import org.bazar.app.api.StorageService;
import org.bazar.app.api.exception.BusinessException;
import org.bazar.domain.File;
import org.bazar.fw.InternalS3;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

import static org.bazar.app.api.exception.ErrorCode.FAILED_TO_DELETE_FILE_FROM_STORAGE;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class StorageServiceAdapter implements StorageService {
    private final S3Presigner s3Presigner;
    private final ConfigProvider configProvider;
    private final @InternalS3 S3Client s3Client;

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

    @Override
    public void deleteByObjectKey(String objectKey) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(configProvider.getFilesBucketName())
                .key(objectKey)
                .build();

        try {
            s3Client.deleteObject(deleteRequest);
        } catch (S3Exception e) {
            throw new BusinessException(FAILED_TO_DELETE_FILE_FROM_STORAGE, objectKey);
        }
    }
}
