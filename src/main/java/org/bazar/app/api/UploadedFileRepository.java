package org.bazar.app.api;

import org.bazar.domain.File;

/**
 * Репозиторий для работы с файлами
 */
public interface UploadedFileRepository {
    /**
     * Сохраняет файл в хранилище
     */
    void save(File file);
}
