package org.bazar.adapter.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bazar.adapter.kafka.message.FileEventOut;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class FileEventProducer {
    @Inject
    @Channel("file.events")
    Emitter<FileEventOut> emitter;

    public void send(FileEventOut message) {
        emitter.send(message);
    }
}
