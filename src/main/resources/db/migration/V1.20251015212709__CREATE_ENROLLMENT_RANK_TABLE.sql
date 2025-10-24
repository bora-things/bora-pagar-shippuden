CREATE TABLE enrollment_rank
(
    id                      BIGSERIAL PRIMARY KEY,
    component_code          VARCHAR(255) NOT NULL,
    class_id                BIGSERIAL    NOT NULL,
    student_id              BIGINT       NOT NULL,
    rank_position           INT          NOT NULL,
    priority_type_id        BIGINT       NOT NULL,
    component_name          VARCHAR(255),
    enrollment_component_id BIGINT,
    processing_timestamp    TIMESTAMP    NOT NULL,
    uncertain_rank          BOOLEAN
);

CREATE INDEX idx_enrollment_rank_component_code ON enrollment_rank (component_code);

CREATE INDEX idx_enrollment_rank_component_code_rank_position ON enrollment_rank (component_code, rank_position);