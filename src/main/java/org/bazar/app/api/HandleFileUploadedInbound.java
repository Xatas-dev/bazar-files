package org.bazar.app.api;

import org.bazar.app.impl.commands.HandleFileUploadedCommand;

public interface HandleFileUploadedInbound {
    void execute(HandleFileUploadedCommand file);
}
