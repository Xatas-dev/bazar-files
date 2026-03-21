package org.bazar.adapter.kafka.message;

public record FileEventOut(
        String fileName,
        String domain,
        String contentType,
        Long size,
        String fileUuid,
        String status
) {
}
