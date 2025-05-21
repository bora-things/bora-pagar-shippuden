CREATE TABLE IF NOT EXISTS student_subjects_interests
(
    id BIGSERIAL PRIMARY KEY,
    year INTEGER NOT NULL,
    period INTEGER NOT NULL,
    student_id BIGINT,
    subject_code INTEGER,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
)

