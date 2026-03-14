CREATE TABLE file (
    id BIGSERIAL PRIMARY KEY,
    file_uuid UUID NOT NULL,
    domain varchar NOT NULL,
    content_type varchar NOT NULL,
    size integer NOT NULL,
    file_name varchar NOT NULL,
    object_key varchar NOT NULL,
    status varchar NOT NULL,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);