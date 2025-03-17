CREATE TABLE IF NOT EXISTS users
(
    id                       BIGSERIAL PRIMARY KEY,
    user_id                  INTEGER ,
    person_name              TEXT    ,
    login                    TEXT    ,
    institutional_id         BIGINT  ,
    cpf                      BIGINT  ,
    email                    TEXT    ,
    image_url                VARCHAR,
    unit_id                  INTEGER ,
    active                   BOOLEAN ,
    created_at              TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    deleted_at              TIMESTAMPTZ
);
