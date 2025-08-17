CREATE TABLE IF NOT EXISTS components
(
    id                        BIGSERIAL PRIMARY KEY,
    total_workload            INTEGER ,
    co_requisites             TEXT ,
    code                      VARCHAR(255) ,
    department                TEXT ,
    mandatory_subject         BOOLEAN ,
    description                  TEXT ,
    equivalent                TEXT ,
    component_id              INTEGER ,
    curricular_matrix_id      INTEGER ,
    name                      VARCHAR(255) ,
    pre_requisites            TEXT ,
    created_at                TIMESTAMPTZ DEFAULT NOW(),
    updated_at                TIMESTAMPTZ DEFAULT NOW(),
    deleted_at                TIMESTAMPTZ
);