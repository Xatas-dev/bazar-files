package org.bazar.adapter.kafka.message;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public record FileEventOut(
        String fileName,
        String domain,
        String contentType,
        Long size,
        String fileUuid,
        String status,
        List<Error> errors
) {
    @RegisterForReflection
    public record Error(
            String code,
            String description
    ) {
    }
}
