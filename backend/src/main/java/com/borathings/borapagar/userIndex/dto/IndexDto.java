package com.borathings.borapagar.userIndex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IndexDto {
    @JsonProperty("id-discente")
    private Integer idDiscente;
    @JsonProperty("id-indice-academico")
    private Integer idIndiceAcademico;
    @JsonProperty("id-indice-discente")
    private Integer idIndiceDiscente;
    @JsonProperty("valor_indice")
    private String valor;
}
