package org.bazar.adapter.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record S3WebhookEvent(
        @JsonProperty("EventName") String eventName,
        @JsonProperty("Key") String key,
        @JsonProperty("Records") List<Record> records
) {

    public record Record(S3 s3) {}

    public record S3(S3Object object) {}

    public record S3Object(
            String key,
            Integer size,
            String contentType
    ) {}
}

