CREATE TABLE IF NOT EXISTS users
(
    id                       BIGSERIAL PRIMARY KEY,
    user_id                  INTEGER NOT NULL,
    person_name              TEXT    NOT NULL,
    login                    TEXT    NOT NULL,
    institutional_id         BIGINT  NOT NULL,
    cpf                      BIGINT  NOT NULL,
    email                    TEXT    NOT NULL,
    image_url                VARCHAR,
    unit_id                  INTEGER NOT NULL,
    active                   BOOLEAN NOT NULL,
    created_at              TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    deleted_at              TIMESTAMPTZ
);
