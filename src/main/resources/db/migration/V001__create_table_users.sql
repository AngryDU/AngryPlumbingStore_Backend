CREATE TABLE IF NOT EXISTS users
(
    id                  UUID DEFAULT gen_random_uuid()      UNIQUE,
    email               CHARACTER VARYING(100)              NOT NULL,
    "password"          CHARACTER VARYING(150)              NOT NULL,
    first_name          CHARACTER VARYING(100),
    last_name           CHARACTER VARYING(100),
    status              CHARACTER VARYING(100),
    phone               VARCHAR(255),
    address             VARCHAR(255),
    about_yourself      TEXT,
    is_active           BOOLEAN                             NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);
