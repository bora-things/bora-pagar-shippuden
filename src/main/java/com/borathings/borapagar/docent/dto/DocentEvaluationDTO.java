package com.borathings.borapagar.docent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DocentEvaluationDTO {

    @JsonProperty("docente")
    private String teacherName;

    @JsonProperty("id-docente")
    private Long teacherId;

    @JsonProperty("media-geral")
    private Double generalAverage;

    private String cpf;
}
