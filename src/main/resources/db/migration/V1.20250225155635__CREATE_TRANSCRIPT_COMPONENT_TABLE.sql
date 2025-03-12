CREATE TABLE IF NOT EXISTS transcript_components
(
    id BIGSERIAL PRIMARY KEY,
    absences INTEGER ,
    register_date BIGINT,
    year INTEGER,
    class_id INTEGER ,
    period INTEGER ,
    component_id BIGINT,
    situation VARCHAR(100),
    integralization VARCHAR(100),
    student_id BIGINT ,
    sigaa_class_id INTEGER ,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
)

