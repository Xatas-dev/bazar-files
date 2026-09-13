package org.bazar.adapter.persistence.outbox;

import org.bazar.domain.outbox.OutboxEvent;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta")
public interface OutboxEventPersistenceMapper {
    List<OutboxEvent> toDomain(List<OutboxEventEntity> entity);

    OutboxEvent toDomain(OutboxEventEntity entity);
}
