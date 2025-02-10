CREATE TABLE workload
(
    id                       BIGSERIAL PRIMARY KEY,
    total_workload_completed INT,
    total_min_workload       INT,
    pending_workload         INT,
    student_id               BIGINT UNIQUE NOT NULL,
    created_at                TIMESTAMPTZ DEFAULT NOW(),
    updated_at                TIMESTAMPTZ DEFAULT NOW(),
    deleted_at                TIMESTAMPTZ,
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
);