CREATE TABLE IF NOT EXISTS components
(
    id                        BIGSERIAL PRIMARY KEY,
    total_workload            INTEGER NOT NULL,
    co_requisites             TEXT NOT NULL,
    code                      VARCHAR(255) NOT NULL,
    block_components          TEXT,
    department                TEXT NOT NULL,
    activity_type_description TEXT NOT NULL,
    mandatory_subject         BOOLEAN NOT NULL,
    description                  TEXT NOT NULL,
    equivalent                TEXT NOT NULL,
    component_id              INTEGER NOT NULL,
    curricular_matrix_id      INTEGER NOT NULL,
    activity_type_id          INTEGER NOT NULL,
    component_type_id         INTEGER NOT NULL,
    unit_id                   INTEGER NOT NULL,
    level                     VARCHAR(100) NOT NULL,
    name                      VARCHAR(255) NOT NULL,
    number_units              INTEGER NOT NULL,
    pre_requisites            TEXT NOT NULL,
    offered_semester          INTEGER NOT NULL,
    created_at                TIMESTAMPTZ DEFAULT NOW(),
    updated_at                TIMESTAMPTZ DEFAULT NOW(),
    deleted_at                TIMESTAMPTZ
);