CREATE TABLE students
(
    id BIGSERIAL PRIMARY KEY,
    student_id INT NOT NULL UNIQUE,
    student_name VARCHAR(255) NOT NULL,
    enrollment_id VARCHAR(255) NOT NULL,
    situation VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    course_id INT NOT NULL,
    course_name VARCHAR(255) NOT NULL,
    level VARCHAR(255) NOT NULL,
    admission_year INT NOT NULL,
    admission_semester INT NOT NULL,
    ingress_method_id INT NOT NULL,
    ingress_method_description VARCHAR(255) NOT NULL,
    academic_manager_id INT NOT NULL,
    participant_type_id INT NOT NULL,
    educational_institution_id INT NOT NULL,
    educational_institution VARCHAR(255) NOT NULL,
    id_campus INT NOT NULL,
    campus VARCHAR(255) NOT NULL,
    index_id BIGINT UNIQUE,
    user_id BIGINT NOT NULL UNIQUE,
    workload_id BIGINT UNIQUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ
);

