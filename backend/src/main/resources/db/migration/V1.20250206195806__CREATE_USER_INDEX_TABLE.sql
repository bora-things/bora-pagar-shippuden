CREATE TABLE IF NOT EXISTS indices
(
    id         BIGSERIAL PRIMARY KEY,
    mc         VARCHAR,
    ira        VARCHAR,
    mcn        VARCHAR,
    iech       VARCHAR,
    iepl       VARCHAR,
    iea        VARCHAR,
    iean       VARCHAR,
    cr         VARCHAR,
    ispl       VARCHAR,
    iechp      VARCHAR,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ,
    user_id    BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
)