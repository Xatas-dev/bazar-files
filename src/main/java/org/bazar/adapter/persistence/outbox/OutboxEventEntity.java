package org.bazar.adapter.persistence.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.bazar.adapter.persistence.DomainObject;

import java.time.Instant;

@Entity
@Table(name = "outbox_event")
@Setter
@Getter
public class OutboxEventEntity extends DomainObject {
    @Column(name = "topic")
    private String topic;

    @Column(name = "status")
    private String status;

    @Column(name = "message", columnDefinition = "jsonb")
    private String message;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "last_error")
    private String lastError;

    @Column(name = "sent_at")
    private Instant sentAt;
}
