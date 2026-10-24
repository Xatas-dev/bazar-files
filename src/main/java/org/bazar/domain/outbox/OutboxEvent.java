package org.bazar.domain.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

import static org.bazar.domain.outbox.OutboxEventStatus.FAILED;
import static org.bazar.domain.outbox.OutboxEventStatus.PENDING;
import static org.bazar.domain.outbox.OutboxEventStatus.SENT;

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
    private Instant nextAttemptAt;
    private Instant createdAt;
    private Instant updatedAt;

    public void markAsSent() {
        sentAt = Instant.now();
        status = SENT;
    }

    public void markAsSendFailed(String errorMessage, int maxRetryCount) {
        lastError = errorMessage;
        retryCount++;

        if (retryCount >= maxRetryCount) {
            status = FAILED;
            return;
        }

        nextAttemptAt = Instant.now().plusSeconds(5L * retryCount);
        status = PENDING;
    }
}
