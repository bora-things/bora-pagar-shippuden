CREATE TABLE IF NOT EXISTS indexes
(
    id BIGSERIAL PRIMARY KEY,
    sigaa_index_id BIGINT ,
    sigaa_student_index_id BIGINT ,
    name VARCHAR ,
    value VARCHAR ,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ,
    student_id BIGINT ,
    FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
)

