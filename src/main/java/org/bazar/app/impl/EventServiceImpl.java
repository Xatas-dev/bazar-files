package org.bazar.app.impl;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.bazar.app.api.OutboxRepository;
import org.bazar.app.api.EventService;
import org.bazar.app.impl.mapper.OutboxMapper;
import org.bazar.app.impl.output.FileEventOut;
import org.bazar.domain.outbox.OutboxEvent;

@ApplicationScoped
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final OutboxRepository outboxRepository;
    private final OutboxMapper outboxMapper;

    @Override
    public void saveFileEvent(FileEventOut file) {
        OutboxEvent fileEvent = outboxMapper.toPendingFileEvent(file);
        outboxRepository.save(fileEvent);
    }
}
