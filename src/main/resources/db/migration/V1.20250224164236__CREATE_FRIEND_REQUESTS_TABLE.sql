CREATE TYPE friend_request_status as ENUM('PENDING','ACCEPTED','REJECTED');

CREATE TABLE IF NOT EXISTS friend_requests
(
    id         BIGSERIAL PRIMARY KEY,
    to_id      INT,
    from_id    INT,
    status     friend_request_status DEFAULT 'PENDING' ,
    created_at TIMESTAMP                                  DEFAULT NOW(),
    updated_at TIMESTAMP                                  DEFAULT NOW(),
    CONSTRAINT fk_to FOREIGN KEY (to_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_from FOREIGN KEY (from_id) REFERENCES users (id) ON DELETE CASCADE
);