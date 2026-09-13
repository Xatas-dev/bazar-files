package org.bazar.domain.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {
    private Long id;
    private String topic;
    private OutboxEventStatus status;
    private String message;
    private Integer retryCount;
    private String lastError;
    private Instant sentAt;
    private Instant createdAt;
    private Instant updatedAt;
}
