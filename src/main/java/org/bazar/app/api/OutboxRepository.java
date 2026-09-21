package org.bazar.app.api;

import org.bazar.domain.outbox.OutboxEvent;
import org.bazar.domain.outbox.OutboxEventStatus;

import java.util.List;

public interface OutboxRepository {
    List<OutboxEvent> findByStatus(OutboxEventStatus status);

    void save(OutboxEvent event);
}
