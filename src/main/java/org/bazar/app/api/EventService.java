package org.bazar.app.api;

import org.bazar.app.impl.output.FileEventOut;

public interface EventService {
    void saveFileEvent(FileEventOut file);
}
