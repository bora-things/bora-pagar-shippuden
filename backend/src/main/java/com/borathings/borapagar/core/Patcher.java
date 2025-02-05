package com.borathings.borapagar.core;

import java.lang.reflect.Field;
import org.springframework.stereotype.Component;

@Component
/**
 * Classe para fazer update parcial de qualquer objeto. Os campos não presentes no segundo objeto não serão alterados.
 *
 * @param <Entity> Tipo de entidade que o Patcher trabalha
 */
public class Patcher<Entity> {
    /**
     * Atualiza a entidade alvo com base nos dados recebidos da fonte. Os campos nulos na fonte não serão sobrescritos
     * no alvo. Este método modifica a entidade alvo. Os campos privados são forçados para serem acessíveis.
     *
     * @param target - Entidade alvo da atualização
     * @param source - Fonte dos novos dados
     * @throws IllegalAccessException - Caso não consiga acessar os campos da entidade
     */
    public void patch(Entity target, Entity source) throws IllegalAccessException {
        Field[] sourceFields = source.getClass().getDeclaredFields();
        for (Field field : sourceFields) {
            field.setAccessible(true);
            Object value = field.get(source);
            if (value != null) {
                field.set(target, value);
            }
            field.setAccessible(false);
        }
    }
}
