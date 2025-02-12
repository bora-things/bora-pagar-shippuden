package com.borathings.borapagar.student.index;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IndexEnum {
    MC(1),
    IRA(2),
    MCN(3),
    IECH(4),
    IEPL(5),
    IEA(6),
    IEAN(7),
    CR(8), // Apenas pós-graduação
    ISPL(9),
    IECHP(10);

    private static final Map<Integer, IndexEnum> ID_MAP = new HashMap<>();

    static {
        for (IndexEnum indice : IndexEnum.values()) {
            ID_MAP.put(indice.id, indice);
        }
    }

    private final int id;

    public static IndexEnum fromId(int id) {
        IndexEnum indice = ID_MAP.get(id);
        if (indice == null) {
            throw new IllegalArgumentException("ID de índice inválido: " + id);
        }
        return indice;
    }
}
