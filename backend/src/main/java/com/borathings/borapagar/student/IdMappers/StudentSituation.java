package com.borathings.borapagar.student.IdMappers;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudentSituation {
    DESCONHECIDO(-1, "DESCONHECIDO"),
    CADASTRADO(2, "CADASTRADO"),
    CANCELADO(6, "CANCELADO"),
    NAO_CADASTRADO(10, "NÃO CADASTRADO"),
    ATIVO(1, "ATIVO"),
    ATIVO_FORMANDO(8, "ATIVO - FORMANDO"),
    CONCLUIDO(3, "CONCLUÍDO"),
    EM_HOMOLOGACAO(11, "EM HOMOLOGAÇÃO"),
    DEFENDIDO(12, "DEFENDIDO"),
    TRANCADO(5, "TRANCADO"),
    PENDENTE_CADASTRO(13, "PENDENTE DE CADASTRO"),
    ATIVO_DEPENDENCIA(14, "ATIVO - DEPENDÊNCIA"),
    FORMADO(9, "FORMADO"),
    PRE_CADASTRADO(15, "PRÉ-CADASTRADO");

    private final int id;
    private final String description;
    private static final Map<Integer, String> lookup = new HashMap<>();

    static {
        for (StudentSituation situation : StudentSituation.values()) {
            lookup.put(situation.id, situation.description);
        }
    }

    public static String getDescriptionById(int id) {
        return lookup.getOrDefault(id, "");
    }
}
