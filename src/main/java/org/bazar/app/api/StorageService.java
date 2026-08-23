package org.bazar.app.api;

import org.bazar.domain.File;
import org.bazar.domain.FileMetadata;

/**
 * Интерфейс для получения presigned URL'ов для работы с хранилищем
 */
public interface StorageService {
    String getUploadUrl(File file);

    String getDownloadUrl(File file);

    void deleteByObjectKey(String objectKey);

    FileMetadata getFileMetadata(File file);
}
