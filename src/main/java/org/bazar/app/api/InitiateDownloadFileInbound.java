package org.bazar.app.api;

import org.bazar.app.impl.commands.InitiateDownloadCommand;

public interface InitiateDownloadFileInbound {
    String execute(InitiateDownloadCommand command);
}
