package org.bazar.app.impl.commands;

public record HandleFileUploadedCommand(
        String key,
        Long size,
        String contentType
) {
}
