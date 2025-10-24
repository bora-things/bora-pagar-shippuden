package com.borathings.borapagar.enrollmentRank.enums;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Representa a prioridade de matrícula de acordo com o Art. 227 das normas da UFRN.
 * A ordem de declaração dos enums (de INGRESSANTE a OUTROS) define a prioridade de classificação.
 * INGRESSANTE é o mais prioritário, OUTROS é o menos prioritário.
 */
public enum PriorityType {
    INGRESSANTE(2, "INGRESSANTE"),
    NIVELADO(3, "NIVELADO"),
    FORMANDO(4, "FORMANDO"),
    EM_RECUPERACAO(5, "EM RECUPERAÇÃO"),
    ADIANTADO(6, "ADIANTADO"),
    ELETIVO(7, "ELETIVO"),
    OUTROS(8, "OUTROS"),
    EM_ANALISE(1, "EM ANÁLISE");

    private final long id;
    private final String description;

    private static final Map<Long, PriorityType> BY_ID = Stream.of(values())
            .collect(Collectors.toMap(PriorityType::getId, Function.identity()));

    PriorityType(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }


    public static PriorityType fromId(long id) {
        return BY_ID.getOrDefault(id, OUTROS);
    }
}