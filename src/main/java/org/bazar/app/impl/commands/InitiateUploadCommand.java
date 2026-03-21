package org.bazar.app.impl.commands;

public record InitiateUploadCommand(
        String fileName,
        Long size,
        String contentType,
        String domain
) {
}
