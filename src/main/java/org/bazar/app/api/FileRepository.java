package org.bazar.app.api;

import org.bazar.domain.File;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с файлами
 */
public interface FileRepository {
    /**
     * Сохраняет файл в БД
     */
    void create(File file);

    void update(File file);

    Optional<File> findByObjectKey(String objectKey);

    Optional<File> findByIdFileUuid(UUID fileUuid);

}
