CREATE TABLE IF NOT EXISTS components_vector_store (
	id BIGSERIAL PRIMARY KEY,
	content text,
	component_id BIGINT,
	embedding_model text,
	embedding vector(1536),
	FOREIGN KEY (component_id) REFERENCES components (id) ON DELETE CASCADE
);
