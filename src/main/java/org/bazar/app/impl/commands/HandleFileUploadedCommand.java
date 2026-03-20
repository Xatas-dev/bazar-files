package org.bazar.app.impl.commands;

public record HandleFileUploadedCommand(
        String key,
        Integer size,
        String contentType
) {
}
