package org.bazar.app.impl.commands;

import org.bazar.domain.file.FileAccessType;

public record InitiateUploadCommand(
        String fileName,
        Long size,
        String contentType,
        String domain,
        FileAccessType accessType
) {
}
