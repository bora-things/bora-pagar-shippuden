CREATE TABLE IF NOT EXISTS users (
	id BIGSERIAL PRIMARY KEY,
	id_usuario TEXT NOT NULL,
	id_discente TEXT NOT NULL,
	id_institucional  TEXT NOT NULL,
	cpf TEXT NOT NULL,
	email TEXT NOT NULL,
	name  TEXT NOT NULL,
	image_url VARCHAR,
	created_at TIMESTAMPTZ,
	updated_at TIMESTAMPTZ,
	deleted_at TIMESTAMPTZ
);
