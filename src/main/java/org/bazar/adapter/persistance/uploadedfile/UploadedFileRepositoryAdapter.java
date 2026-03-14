package org.bazar.adapter.persistance.uploadedfile;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.bazar.app.api.UploadedFileRepository;
import org.bazar.domain.File;

import java.util.List;

@ApplicationScoped
public class UploadedFileRepositoryAdapter implements UploadedFileRepository {
    private final EntityManager entityManager;
    private final UploadedFileMapper uploadedFileMapper;

    @Inject
    public UploadedFileRepositoryAdapter(EntityManager entityManager, UploadedFileMapper uploadedFileMapper) {
        this.entityManager = entityManager;
        this.uploadedFileMapper = uploadedFileMapper;
    }

    @Override
    public void save(File file) {
        FileEntity entity = uploadedFileMapper.toEntity(file);
        entityManager.persist(entity);
    }

    @Override
    public List<File> findAll() {
        return entityManager
                .createQuery("SELECT e FROM FileEntity e", FileEntity.class)
                .getResultList()
                .stream()
                .map(uploadedFileMapper::toDomain)
                .toList();
    }
}
