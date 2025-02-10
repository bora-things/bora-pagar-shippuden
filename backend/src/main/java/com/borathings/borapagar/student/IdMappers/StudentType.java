package com.borathings.borapagar.student.IdMappers;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum StudentType {
    REGULAR(1),
    ESPECIAL(2);

    private final int id;
    private static final Map<Integer, StudentType> lookup = new HashMap<>();

    static {
        for (StudentType situation : StudentType.values()) {
            lookup.put(situation.id, situation);
        }
    }

    public static StudentType getSituationDescriptionById(int id) {
        StudentType type = lookup.get(id);
        if (type == null) {
            throw new IllegalArgumentException("ID de tipo inválido: " + id);
        }
        return type;
    }
}
