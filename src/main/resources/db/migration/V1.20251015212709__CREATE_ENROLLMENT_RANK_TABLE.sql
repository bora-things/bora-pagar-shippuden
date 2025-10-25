CREATE TABLE enrollment_rank
(
    id                      BIGSERIAL PRIMARY KEY,
    year                    INT          NOT NULL,
    period                  INT          NOT NULL,
    class_id                BIGSERIAL    NOT NULL,
    student_id              BIGINT       NOT NULL,
    rank_position           INT          NOT NULL,
    priority_type_id        BIGINT       NOT NULL,
    processing_timestamp    TIMESTAMP    NOT NULL,
    uncertain_rank          BOOLEAN
);
