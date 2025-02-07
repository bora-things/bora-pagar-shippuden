CREATE TABLE IF NOT EXISTS turmas
(
    id                         BIGSERIAL PRIMARY KEY,
    id_turma                   BIGINT  NOT NULL UNIQUE,
    ano                        INTEGER NOT NULL,
    capacidade_aluno           INTEGER NOT NULL,
    codigo_componente          TEXT    NOT NULL,
    codigo_turma               TEXT    NOT NULL,
    descricao_horario          TEXT,
    id_componente              BIGINT,
    id_discente                BIGINT,
    id_docente                 BIGINT,
    id_docente_externo         BIGINT,
    id_modalidade_educacao     BIGINT,
    id_situacao_turma          BIGINT,
    id_turma_agrupadora        BIGINT,
    id_unidade                 BIGINT,
    local                      TEXT,
    nome_componente            TEXT,
    periodo                    INTEGER NOT NULL,
    sigla_nivel                TEXT,
    subturma                   BOOLEAN NOT NULL,
    tipo                       INTEGER NOT NULL,
    utiliza_nova_turma_virtual BOOLEAN NOT NULL,
    created_at                 TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at                 TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    deleted_at                 TIMESTAMPTZ,
    user_id                    BIGINT  NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    componente_id BIGINT NOT NULL,
    FOREIGN KEY (componente_id) REFERENCES componentes (id) ON DELETE CASCADE,
    docente_id BIGINT NOT NULL,
    FOREIGN KEY (docente_id) REFERENCES docentes (id) ON DELETE CASCADE



);
