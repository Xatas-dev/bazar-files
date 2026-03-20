package org.bazar.adapter.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bazar.app.api.NotifyFileUploadedOutbound;
import org.bazar.domain.File;

@ApplicationScoped
public class FileEventProducerAdapter implements NotifyFileUploadedOutbound {
    private final FileEventProducer fileEventProducer;
    private final KafkaFileMapper kafkaFileMapper;

    @Inject
    public FileEventProducerAdapter(FileEventProducer fileEventProducer, KafkaFileMapper kafkaFileMapper) {
        this.fileEventProducer = fileEventProducer;
        this.kafkaFileMapper = kafkaFileMapper;
    }

    @Override
    public void execute(File event) {
        fileEventProducer.send(kafkaFileMapper.toFileEventOut(event));
    }
}
