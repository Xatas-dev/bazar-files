package org.bazar.app.api;

import org.bazar.domain.File;

import java.util.List;
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

    void deleteById(Long id);

    Optional<File> findByObjectKey(String objectKey);

    Optional<File> findByFileUuid(UUID fileUuid);

    List<File> findDeletingFilesAfterId(Long lastId, Integer batch);
}
