package org.bazar.app.api;

public interface ConfigProvider {
    String getFilesBucketName();
    int getUploadUrlTtl();
    int getDownloadUrlTtl();
    int getDeletingFilesBatchSize();
}
