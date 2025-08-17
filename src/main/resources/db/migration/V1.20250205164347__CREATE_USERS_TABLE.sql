CREATE TABLE IF NOT EXISTS users
(
    id                       BIGSERIAL PRIMARY KEY,
    user_id                  INTEGER ,
    person_name              TEXT    ,
    login                    TEXT    ,
    institutional_id         BIGINT  ,
    image_url                VARCHAR,
    created_at              TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    deleted_at              TIMESTAMPTZ
);
