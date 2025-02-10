CREATE TABLE IF NOT EXISTS indices
(
    id         BIGSERIAL PRIMARY KEY,
    mc         VARCHAR,
    ira        VARCHAR,
    mcn        VARCHAR,
    iech       VARCHAR,
    iepl       VARCHAR,
    iea        VARCHAR,
    iean       VARCHAR,
    cr         VARCHAR,
    ispl       VARCHAR,
    iechp      VARCHAR,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ,
    student_id    BIGSERIAL NOT NULL,
    FOREIGN KEY (student_id) REFERENCES student (id) ON DELETE CASCADE
)