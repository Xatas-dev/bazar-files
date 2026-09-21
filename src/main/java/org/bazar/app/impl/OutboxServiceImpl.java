package org.bazar.app.impl;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.bazar.app.api.OutboxRepository;
import org.bazar.app.api.OutboxService;
import org.bazar.app.impl.mapper.OutboxMapper;
import org.bazar.domain.file.File;
import org.bazar.domain.outbox.OutboxEvent;

@ApplicationScoped
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {
    private final OutboxRepository outboxRepository;
    private final OutboxMapper outboxMapper;

    @Override
    public void saveFileEvent(File file) {
        OutboxEvent fileEvent = outboxMapper.toPendingFileEvent(file);
        outboxRepository.save(fileEvent);
    }
}
