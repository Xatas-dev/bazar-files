package org.bazar.adapter.kafka;

import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Message;

@ApplicationScoped
public class KafkaEventProducer {

    @Inject
    @Channel("outbox-events")
    MutinyEmitter<String> emitter;

    public Uni<Void> send(String topic, String message) {
        OutgoingKafkaRecordMetadata<String> metadata =
                OutgoingKafkaRecordMetadata.<String>builder()
                        .withTopic(topic)
                        .build();

        return emitter.sendMessage(Message.of(message).addMetadata(metadata));
    }
}
