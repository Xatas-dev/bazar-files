package org.bazar.adapter.persistence.outbox;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.bazar.app.api.OutboxRepository;
import org.bazar.domain.outbox.OutboxEvent;
import org.bazar.domain.outbox.OutboxEventStatus;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class OutboxEventRepositoryAdapter implements OutboxRepository {
    private final EntityManager entityManager;
    private final OutboxEventPersistenceMapper outboxEventPersistenceMapper;

    @Override
    public List<OutboxEvent> findByStatus(OutboxEventStatus status, Integer batchSize) {
        List<OutboxEventEntity> resultList = entityManager.createQuery("""
                            SELECT oe FROM OutboxEventEntity oe
                            WHERE oe.status = :status AND oe.nextAttemptAt <= :now
                            ORDER BY oe.createdAt
                        """, OutboxEventEntity.class)
                .setParameter("status", status.name())
                .setParameter("now", Instant.now())
                .setMaxResults(batchSize)
                .getResultList();
        return outboxEventPersistenceMapper.toDomain(resultList);
    }

    @Override
    public void save(OutboxEvent event) {
        OutboxEventEntity entity = outboxEventPersistenceMapper.toEntity(event);
        entityManager.persist(entity);
    }

    @Override
    public void update(OutboxEvent event) {
        OutboxEventEntity entity = outboxEventPersistenceMapper.toEntity(event);
        entityManager.merge(entity);
    }
}
