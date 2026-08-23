package org.bazar.adapter.s3;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.mime.MimeTypeException;
import org.bazar.app.api.ConfigProvider;
import org.bazar.app.api.StorageService;
import org.bazar.app.api.exception.BusinessException;
import org.bazar.app.api.exception.TechnicalException;
import org.bazar.domain.File;
import org.bazar.domain.FileMetadata;
import org.bazar.fw.InternalS3;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;

import static org.bazar.app.api.exception.ErrorCode.FAILED_TO_DELETE_FILE_FROM_STORAGE;

@ApplicationScoped
@Slf4j
public class StorageServiceAdapter implements StorageService {
    private static final String RANGE = "bytes=0-16383";

    private final S3Presigner s3Presigner;
    private final ConfigProvider configProvider;
    private final @InternalS3 S3Client s3Client;
    private final ContentTypeResolver contentTypeResolver;
    private final StorageMapper storageMapper;

    @Inject
    public StorageServiceAdapter(S3Presigner s3Presigner, ConfigProvider configProvider, @InternalS3 S3Client s3Client, ContentTypeResolver contentTypeResolver, StorageMapper storageMapper) {
        this.s3Presigner = s3Presigner;
        this.configProvider = configProvider;
        this.s3Client = s3Client;
        this.contentTypeResolver = contentTypeResolver;
        this.storageMapper = storageMapper;
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

    @Override
    public FileMetadata getFileMetadata(File file) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(configProvider.getFilesBucketName())
                .key(file.getObjectKey())
                .range(RANGE)
                .build();

        try {
            ResponseInputStream<GetObjectResponse> storageStreamResponse = s3Client.getObject(getObjectRequest);
            String contentType = contentTypeResolver.getContentTypeByBytes(storageStreamResponse.readAllBytes());
            return storageMapper.toFileMetadata(storageStreamResponse.response(), contentType, file);
        } catch (S3Exception | MimeTypeException | IOException e) {
            log.error("Failed to get file metadata", e);
            throw new TechnicalException("Failed to get file metadata");
        }
    }
}
