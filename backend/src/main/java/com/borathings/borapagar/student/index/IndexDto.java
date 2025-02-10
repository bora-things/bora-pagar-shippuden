package com.borathings.borapagar.student.index;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IndexDto {
    @JsonProperty("id-discente")
    private Integer studentId;
    @JsonProperty("id-indice-academico")
    private Integer academicIndexId;
    @JsonProperty("id-indice-discente")
    private Integer studentIndexId;
    @JsonProperty("valor_indice")
    private String value;
}
