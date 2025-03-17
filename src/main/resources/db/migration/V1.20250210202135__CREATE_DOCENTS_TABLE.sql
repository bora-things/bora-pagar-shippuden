CREATE TABLE IF NOT EXISTS docents
(
    id                        BIGSERIAL PRIMARY KEY,
    position                  TEXT ,
    cpf                       TEXT ,
    admission_date            BIGINT ,
    siape_digit               TEXT ,
    email                     TEXT ,
    is_active                 INTEGER ,
    position_id               INTEGER ,
    teacher_id                INTEGER ,
    institutional_id          INTEGER ,
    status_id                 INTEGER ,
    unit_id                   INTEGER ,
    name                      TEXT ,
    identification_name       TEXT ,
    work_regime               INTEGER ,
    gender                    TEXT ,
    siape                     INTEGER ,
    unit                      TEXT ,
    created_at                TIMESTAMPTZ DEFAULT NOW(),
    updated_at                TIMESTAMPTZ DEFAULT NOW(),
    deleted_at                TIMESTAMPTZ
);
