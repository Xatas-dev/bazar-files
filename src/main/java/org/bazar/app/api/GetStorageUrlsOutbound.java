package org.bazar.app.api;

import org.bazar.domain.File;

/**
 * Интерфейс для получения presigned URL'ов для работы с хранилищем
 */
public interface GetStorageUrlsOutbound {
    String getUploadUrl(File file);

    String getDownloadUrl(File file);
}
