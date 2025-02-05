CREATE TABLE IF NOT EXISTS users (
	id BIGSERIAL PRIMARY KEY,
	id_usuario INTEGER NOT NULL,
	id_discente BIGINT NOT NULL,
	id_institucional  BIGINT NOT NULL,
	cpf BIGINT NOT NULL,
	email TEXT NOT NULL,
	name  TEXT NOT NULL,
	login  TEXT NOT NULL,
	image_url VARCHAR,
	created_at TIMESTAMPTZ,
	updated_at TIMESTAMPTZ,
	deleted_at TIMESTAMPTZ
);
