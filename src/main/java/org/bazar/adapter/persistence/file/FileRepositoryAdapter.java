package org.bazar.adapter.persistence.file;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.bazar.app.api.FileRepository;
import org.bazar.domain.File;

import java.util.Optional;

/**
 * Реализация репозитория для работы с файлами
 */
@ApplicationScoped
public class FileRepositoryAdapter implements FileRepository {
    private final EntityManager entityManager;
    private final FilePersistenceMapper filePersistenceMapper;

    @Inject
    public FileRepositoryAdapter(EntityManager entityManager, FilePersistenceMapper filePersistenceMapper) {
        this.entityManager = entityManager;
        this.filePersistenceMapper = filePersistenceMapper;
    }

    @Override
    public void create(File file) {
        FileEntity entity = filePersistenceMapper.toEntity(file);
        entityManager.persist(entity);
    }

    @Override
    public void update(File file) {
        FileEntity entity = filePersistenceMapper.toEntity(file);
        entityManager.merge(entity);
    }

    @Override
    public Optional<File> findByObjectKey(String objectKey) {
        return Optional.ofNullable(filePersistenceMapper.toDomain(entityManager.createQuery("""
                SELECT f
                FROM FileEntity f
                WHERE f.objectKey = :objectKey
                """, FileEntity.class)
                .setParameter("objectKey", objectKey)
                .getSingleResult()));
    }
}
