CREATE TABLE IF NOT EXISTS docentes
(
    id                 BIGSERIAL PRIMARY KEY,
    cargo              TEXT        NOT NULL,
    cpf                VARCHAR(14) NOT NULL UNIQUE,
    data_admissao      BIGINT      NOT NULL,
    digito_siape       TEXT        NOT NULL, -- REVER TIPO
    email              TEXT        NOT NULL UNIQUE,
    id_ativo           INTEGER     NOT NULL,
    id_cargo           INTEGER     NOT NULL,
    id_docente         INTEGER     NOT NULL UNIQUE,
    id_institucional   INTEGER     NOT NULL,
    id_situacao        INTEGER     NOT NULL,
    id_unidade         INTEGER     NOT NULL,
    nome               TEXT        NOT NULL,
    nome_identificacao TEXT        NOT NULL,
    regime_trabalho    INTEGER     NOT NULL,
    sexo               VARCHAR(10) NOT NULL,
    siape              INTEGER     NOT NULL UNIQUE,
    unidade            TEXT        NOT NULL,
    created_at         TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    deleted_at         TIMESTAMPTZ
);
