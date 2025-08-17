CREATE TABLE IF NOT EXISTS taken_components
(
    id BIGSERIAL PRIMARY KEY,
    year INTEGER,
    period INTEGER,
    component_id BIGINT,
    situation INTEGER,
    student_id BIGINT ,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
)

