CREATE TABLE IF NOT EXISTS transcript_components
(
    id BIGSERIAL PRIMARY KEY,
    absences INTEGER NOT NULL,
    register_date BIGINT,
    year INTEGER,
    class_id INTEGER NOT NULL,
    period INTEGER NOT NULL,
    component_id BIGINT,
    situation VARCHAR(100),
    integralization VARCHAR(100),
    student_id BIGINT NOT NULL,
    sigaa_class_id INTEGER NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
)

