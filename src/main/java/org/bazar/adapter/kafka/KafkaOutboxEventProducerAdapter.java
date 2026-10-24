package org.bazar.adapter.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.bazar.app.api.SendOutboxEventOutbound;
import org.bazar.domain.outbox.OutboxEvent;

import java.time.Duration;

@ApplicationScoped
@RequiredArgsConstructor
public class KafkaOutboxEventProducerAdapter implements SendOutboxEventOutbound {
    private final KafkaEventProducer kafkaEventProducer;

    @Override
    public void execute(OutboxEvent event) {
        kafkaEventProducer.send(event.getTopic(), event.getMessage()).await().atMost(Duration.ofSeconds(10));
    }
}
