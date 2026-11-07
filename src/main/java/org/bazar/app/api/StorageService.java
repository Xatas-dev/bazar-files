package org.bazar.app.api;

import org.bazar.domain.file.File;
import org.bazar.domain.file.FileMetadata;

/**
 * Интерфейс для получения presigned URL'ов для работы с хранилищем
 */
public interface StorageService {
    String getUploadUrl(File file);

    String getDownloadUrl(File file);

    void deleteFile(File file);

    FileMetadata getFileMetadata(File file);

    String getPublicUrl(File file);
}
