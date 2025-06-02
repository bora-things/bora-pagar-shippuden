package com.borathings.borapagar.student.transcript.enums;

public enum TranscriptComponentSituationEnum {
    APROVADO(4),
    REPROVADO(6),
    REPROVADO_POR_FALTAS(7),
    REPROVADO_POR_MEDIA_E_POR_FALTAS(9),
    APROVADO_POR_NOTA(24),
    REPROVADO_POR_NOTA(25),
    REPROVADO_POR_NOTA_E_FALTA(26),
    REPROVADO_EM_TODO_PERIODO_LETIVO(27),
    EM_ESPERA(1),
    MATRICULADO(2),
    CANCELADO(3),
    TRANCADO(5),
    EXCLUIDA(10),
    INDEFERIDO(11),
    DESISTENCIA(12),
    DISPENSADO(21),
    CUMPRIU(22),
    TRANSFERIDO(23),
    AGUARDANDO_DEFERIMENTO(16),
    INCORPORADO(28),
    APROVADO_POR_CONSELHO(30),
    INCORPORADO_TALENTO(31);

    private final int id;

    TranscriptComponentSituationEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static TranscriptComponentSituationEnum fromId(int id) {
        for (TranscriptComponentSituationEnum situation : values()) {
            if (situation.id == id) {
                return situation;
            }
        }
        throw new IllegalArgumentException("Invalid id: " + id);
    }

    public boolean isApproved() {
        return this == APROVADO || this == APROVADO_POR_NOTA || this == APROVADO_POR_CONSELHO || this == CUMPRIU;
    }
}
