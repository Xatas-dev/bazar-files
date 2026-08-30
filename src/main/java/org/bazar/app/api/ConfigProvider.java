package org.bazar.app.api;

import org.bazar.domain.ValidationErrorCode;

import java.util.List;
import java.util.Map;

public interface ConfigProvider {
    String getFilesBucketName();
    int getUploadUrlTtl();
    int getDownloadUrlTtl();
    int getDeletingFilesBatchSize();
    long getMaxFileSize();
    List<String> getNotAllowedExtensions();
    Map<ValidationErrorCode, String> getErrorMessages();
}
