CREATE TABLE IF NOT EXISTS classrooms
(
    id                       BIGSERIAL PRIMARY KEY,
    classroom_id             BIGINT  UNIQUE,
    year                     INTEGER ,
    component_code           VARCHAR(255) ,
    classroom_code           VARCHAR(255) ,
    unit_id                  BIGINT,
    location                 TEXT,
    component_name           TEXT,
    period                 INTEGER ,
    student_id               BIGINT,
    docent_id               BIGINT,
    component_id               BIGINT,
    created_at               TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    deleted_at               TIMESTAMPTZ,
    CONSTRAINT fk_classroom_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_classroom_subject FOREIGN KEY (component_id) REFERENCES components(id)

);
