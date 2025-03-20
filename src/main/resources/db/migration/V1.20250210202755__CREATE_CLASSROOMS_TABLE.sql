CREATE TABLE IF NOT EXISTS classrooms
(
    id                       BIGSERIAL PRIMARY KEY,
    classroom_id             BIGINT  UNIQUE,
    year                     INTEGER ,
    student_capacity         INTEGER ,
    component_code           VARCHAR(255) ,
    classroom_code           VARCHAR(255) ,
    schedule_description     TEXT,
    external_teacher_id      BIGINT,
    education_mode_id        BIGINT,
    classroom_status_id      BIGINT,
    grouping_classroom_id    BIGINT,
    unit_id                  BIGINT,
    location                 TEXT,
    component_name           TEXT,
    semester                 INTEGER ,
    level_abbreviation       VARCHAR(100),
    subgroup                 BOOLEAN ,
    type                     INTEGER ,
    uses_new_virtual_classroom BOOLEAN ,
    student_id               BIGINT,
    docent_id               BIGINT,
    component_id               BIGINT,
    created_at               TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    deleted_at               TIMESTAMPTZ,
    CONSTRAINT fk_classroom_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_classroom_docent FOREIGN KEY (docent_id) REFERENCES docents(id),
    CONSTRAINT fk_classroom_subject FOREIGN KEY (component_id) REFERENCES components(id)
);
