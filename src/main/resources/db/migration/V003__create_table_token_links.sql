CREATE TABLE IF NOT EXISTS token_links
(
    id                  UUID DEFAULT gen_random_uuid() UNIQUE,
    token               CHARACTER VARYING(1000) NOT NULL,
    active_time         BIGINT,
    create_time         TIMESTAMP,
    is_active           BOOLEAN DEFAULT FALSE NOT NULL
);