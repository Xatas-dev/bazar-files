package org.bazar.app.api;

import org.bazar.domain.file.File;

public interface OutboxService {
    void saveFileEvent(File file);
}
