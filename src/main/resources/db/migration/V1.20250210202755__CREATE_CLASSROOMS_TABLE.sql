CREATE TABLE IF NOT EXISTS classrooms
(
    id                       BIGSERIAL PRIMARY KEY,
    classroom_id             BIGINT  UNIQUE,
    year                     INTEGER ,
    component_code           VARCHAR(255) ,
    classroom_code           VARCHAR(255) ,
    unit_id                  BIGINT,
    component_name           TEXT,
    period                 INTEGER ,
    created_at               TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    deleted_at               TIMESTAMPTZ
);
