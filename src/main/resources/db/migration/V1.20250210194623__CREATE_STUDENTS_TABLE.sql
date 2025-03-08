CREATE TABLE students
(
    id BIGSERIAL PRIMARY KEY,
    student_id INT  UNIQUE,
    student_name VARCHAR(255) ,
    enrollment_id VARCHAR(255) ,
    situation VARCHAR(50) ,
    type VARCHAR(50) ,
    course_id INT ,
    course_name VARCHAR(255) ,
    level VARCHAR(255) ,
    admission_year INT ,
    admission_semester INT ,
    ingress_method_id INT ,
    ingress_method_description VARCHAR(255) ,
    academic_manager_id INT ,
    participant_type_id INT ,
    educational_institution_id INT ,
    educational_institution VARCHAR(255) ,
    id_campus INT ,
    campus VARCHAR(255) ,
    index_id BIGINT UNIQUE,
    user_id BIGINT  UNIQUE,
    workload_id BIGINT UNIQUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    deleted_at TIMESTAMPTZ
);

