package org.bazar.app.impl;

import lombok.RequiredArgsConstructor;
import org.bazar.app.api.ConfigProvider;
import org.bazar.app.api.OutboxRepository;
import org.bazar.app.api.SendOutboxEventOutbound;
import org.bazar.app.api.SendOutboxEventsInbound;
import org.bazar.app.api.UnitOfWork;
import org.bazar.domain.outbox.OutboxEvent;

import java.util.List;

import static org.bazar.domain.outbox.OutboxEventStatus.PENDING;

@RequiredArgsConstructor
public class SendOutboxEventsUseCase implements SendOutboxEventsInbound {
    private final OutboxRepository outboxRepository;
    private final SendOutboxEventOutbound sendOutBoxEventOutbound;
    private final ConfigProvider configProvider;
    private final UnitOfWork unitOfWork;

    @Override
    public void execute() {
        List<OutboxEvent> eventsToSend = outboxRepository.findByStatus(PENDING, configProvider.getSendingOutboxEventsBatchSize());

        for (OutboxEvent event : eventsToSend) {
            try {
                sendOutBoxEventOutbound.execute(event);
                unitOfWork.perform(() -> {
                    event.markAsSent();
                    outboxRepository.update(event);
                });
            } catch (Exception e) {
                unitOfWork.perform(() -> {
                    event.markAsSendFailed(e.getMessage(), configProvider.getOutboxMaxRetryCount());
                    outboxRepository.update(event);
                });
            }
        }
    }
}
