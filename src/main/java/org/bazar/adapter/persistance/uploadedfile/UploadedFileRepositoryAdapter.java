package org.bazar.adapter.persistance.uploadedfile;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.bazar.app.api.UploadedFileRepository;
import org.bazar.domain.UploadedFile;

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
    public void save(UploadedFile uploadedFile) {
        UploadedFileEntity entity = uploadedFileMapper.toEntity(uploadedFile);
        entityManager.persist(entity);
    }

    @Override
    public List<UploadedFile> findAll() {
        return entityManager
                .createQuery("SELECT e FROM UploadedFileEntity e", UploadedFileEntity.class)
                .getResultList()
                .stream()
                .map(uploadedFileMapper::toDomain)
                .toList();
    }
}
