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
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ,
    student_id BIGSERIAL NOT NULL,
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
)