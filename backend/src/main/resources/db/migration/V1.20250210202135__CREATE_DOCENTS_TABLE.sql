CREATE TABLE IF NOT EXISTS docents
(
    id                        BIGSERIAL PRIMARY KEY,
    position                  TEXT NOT NULL,
    cpf                       TEXT NOT NULL,
    admission_date            BIGINT NOT NULL,
    siape_digit               TEXT NOT NULL,
    email                     TEXT NOT NULL,
    is_active                 INTEGER NOT NULL,
    position_id               INTEGER NOT NULL,
    teacher_id                INTEGER NOT NULL,
    institutional_id          INTEGER NOT NULL,
    status_id                 INTEGER NOT NULL,
    unit_id                   INTEGER NOT NULL,
    name                      TEXT NOT NULL,
    identification_name       TEXT NOT NULL,
    work_regime               INTEGER NOT NULL,
    gender                    TEXT NOT NULL,
    siape                     INTEGER NOT NULL,
    unit                      TEXT NOT NULL,
    created_at                TIMESTAMPTZ DEFAULT NOW(),
    updated_at                TIMESTAMPTZ DEFAULT NOW(),
    deleted_at                TIMESTAMPTZ
);
