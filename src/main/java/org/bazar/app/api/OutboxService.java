package org.bazar.app.api;

import org.bazar.app.impl.output.FileEventOut;

public interface OutboxService {
    void saveFileEvent(FileEventOut file);
}
