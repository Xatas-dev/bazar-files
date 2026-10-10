package org.bazar.app.api;

import org.bazar.domain.outbox.OutboxEvent;

public interface SendOutBoxEventOutbound {
    void execute(OutboxEvent event);
}
