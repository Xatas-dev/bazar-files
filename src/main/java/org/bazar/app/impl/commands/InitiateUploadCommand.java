package org.bazar.app.impl.commands;

public record InitiateUploadCommand(
        String fileName,
        Integer size,
        String contentType,
        String domain
) {
}
