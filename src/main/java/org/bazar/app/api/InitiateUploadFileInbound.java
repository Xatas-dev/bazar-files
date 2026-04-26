package org.bazar.app.api;

import org.bazar.app.impl.commands.InitiateUploadCommand;
import org.bazar.app.impl.output.FileInfo;

public interface InitiateUploadFileInbound {
    FileInfo execute(InitiateUploadCommand command);
}
