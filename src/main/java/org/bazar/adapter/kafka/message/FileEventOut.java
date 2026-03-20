package org.bazar.adapter.kafka.message;

public record FileEventOut(
        String fileName,
        String domain,
        String contentType,
        Integer size,
        String fileUuid,
        String status
) {
}
