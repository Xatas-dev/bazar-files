package org.bazar.adapter.kafka.message;

import java.util.List;

public record FileEventOut(
        String fileName,
        String domain,
        String contentType,
        Long size,
        String fileUuid,
        String status,
        List<Error> errors
) {
    public record Error(
            String code,
            String description
    ) {
    }
}
