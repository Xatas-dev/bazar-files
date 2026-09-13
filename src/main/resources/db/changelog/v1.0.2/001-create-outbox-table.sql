CREATE TABLE IF NOT EXISTS outbox_event (
    id BIGSERIAL PRIMARY KEY,
    topic VARCHAR NOT NULL,
    status VARCHAR NOT NULL,
    message JSONB NOT NULL,
    retry_count INTEGER,
    last_error TEXT,
    sent_at timestamp with time zone,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);