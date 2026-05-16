package org.bazar.app.api;

import java.util.UUID;

public interface MarkFileForDeletionInbound {
    void execute(UUID fileUuid);
}
