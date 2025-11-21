CREATE TABLE IF NOT EXISTS student_classroom (
    student_id BIGINT NOT NULL,
    classroom_id BIGINT NOT NULL,
    PRIMARY KEY (student_id, classroom_id),

    CONSTRAINT fk_student_classroom_student
    FOREIGN KEY (student_id)
    REFERENCES students (id)
    ON DELETE CASCADE,

    CONSTRAINT fk_student_classroom_classroom
    FOREIGN KEY (classroom_id)
    REFERENCES classrooms (id)
    ON DELETE CASCADE
    );