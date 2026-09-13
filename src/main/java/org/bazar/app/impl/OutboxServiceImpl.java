package org.bazar.app.impl;

import jakarta.enterprise.context.ApplicationScoped;
import org.bazar.app.api.OutboxRepository;
import org.bazar.app.api.OutboxService;
import org.bazar.domain.file.File;

@ApplicationScoped
public class OutboxServiceImpl implements OutboxService {
    private OutboxRepository outboxRepository;

    @Override
    public void saveFileEvent(File file) {

    }
}
