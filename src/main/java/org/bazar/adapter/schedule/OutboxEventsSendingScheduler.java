package org.bazar.adapter.schedule;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.bazar.app.api.SendOutboxEventsInbound;

@ApplicationScoped
@RequiredArgsConstructor
public class OutboxEventsSendingScheduler {
    private final SendOutboxEventsInbound sendOutboxEventsInbound;

    @Scheduled(every = "{settings.scheduler.send-outbox-events.cron}")
    // TODO: посмотреть в сторону @Observes
    void sendOutboxEvents() {
        sendOutboxEventsInbound.execute();
    }
}
