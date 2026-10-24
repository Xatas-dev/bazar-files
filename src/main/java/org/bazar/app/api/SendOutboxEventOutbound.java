package org.bazar.app.api;

import org.bazar.domain.outbox.OutboxEvent;

public interface SendOutboxEventOutbound {
    void execute(OutboxEvent event);
}
