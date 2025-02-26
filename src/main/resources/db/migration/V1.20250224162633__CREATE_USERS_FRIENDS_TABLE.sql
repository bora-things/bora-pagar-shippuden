CREATE TABLE IF NOT EXISTS user_friends
(
    id        BIGSERIAL PRIMARY KEY,
    user_id   INT,
    friend_id INT,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_friend FOREIGN KEY (friend_id) REFERENCES users (id) ON DELETE CASCADE
);