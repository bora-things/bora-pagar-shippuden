package com.borathings.borapagar.student.index;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IndexEnumTest {

    @Test
    void testFromIdValidIndex() {
        IndexEnum index = IndexEnum.fromId(1); // ID existente
        Assertions.assertEquals(IndexEnum.MC, index);
    }

    @Test
    void testFromIdInvalidIndex() {
        int invalidId = 999; // ID inexistente
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    IndexEnum.fromId(invalidId);
                },
                "Deveria lançar exceção para ID inválido.");
    }
}
