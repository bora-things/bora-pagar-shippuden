CREATE TABLE IF NOT EXISTS components
(
    id                        BIGSERIAL PRIMARY KEY,
    total_workload            INTEGER ,
    co_requisites             TEXT ,
    code                      VARCHAR(255) ,
    block_components          TEXT,
    department                TEXT ,
    activity_type_description TEXT ,
    mandatory_subject         BOOLEAN ,
    description                  TEXT ,
    equivalent                TEXT ,
    component_id              INTEGER ,
    curricular_matrix_id      INTEGER ,
    activity_type_id          INTEGER ,
    component_type_id         INTEGER ,
    unit_id                   INTEGER ,
    level                     VARCHAR(100) ,
    name                      VARCHAR(255) ,
    number_units              INTEGER ,
    pre_requisites            TEXT ,
    offered_semester          INTEGER ,
    created_at                TIMESTAMPTZ DEFAULT NOW(),
    updated_at                TIMESTAMPTZ DEFAULT NOW(),
    deleted_at                TIMESTAMPTZ
);