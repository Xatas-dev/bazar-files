package org.bazar.adapter.persistence.file;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.bazar.app.api.UploadedFileRepository;
import org.bazar.domain.File;

/**
 * Реализация репозитория для работы с файлами
 */
@ApplicationScoped
public class UploadedFileRepositoryAdapter implements UploadedFileRepository {
    private final EntityManager entityManager;
    private final FilePersistenceMapper filePersistenceMapper;

    @Inject
    public UploadedFileRepositoryAdapter(EntityManager entityManager, FilePersistenceMapper filePersistenceMapper) {
        this.entityManager = entityManager;
        this.filePersistenceMapper = filePersistenceMapper;
    }

    @Override
    public void save(File file) {
        FileEntity entity = filePersistenceMapper.toEntity(file);
        entityManager.persist(entity);
    }
}
