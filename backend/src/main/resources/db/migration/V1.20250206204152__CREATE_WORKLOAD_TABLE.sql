CREATE TABLE IF NOT EXISTS carga_horaria
(
    id                     BIGSERIAL PRIMARY KEY,
    ch_total_integralizada INTEGER,
    ch_total_min           INTEGER,
    ch_total_pendente      INTEGER,
    created_at             TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    deleted_at             TIMESTAMPTZ,
    user_id                BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
)