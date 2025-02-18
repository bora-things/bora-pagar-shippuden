 CREATE TABLE IF NOT EXISTS indexes
  (
     id         BIGSERIAL PRIMARY KEY,
     name       VARCHAR NOT NULL,
     value      VARCHAR NOT NULL,
     created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
     deleted_at TIMESTAMPTZ,
     student_id BIGINT NOT NULL,
     student_index_id BIGINT NOT NULL,
     FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
  )  

