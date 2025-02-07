CREATE TABLE IF NOT EXISTS componentes_bloco
(
    id         BIGSERIAL PRIMARY KEY,
    subject_id BIGINT NOT NULL,
    bloco      VARCHAR(255),
    FOREIGN KEY (subject_id) REFERENCES componentes (id) ON DELETE CASCADE
);
