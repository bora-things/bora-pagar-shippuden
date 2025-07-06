package com.borathings.borapagar.docent.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DocentEvaluationDTO {

    @JsonProperty("id-avaliacao")
    private Long evaluationId;

    @JsonProperty("docente")
    private String teacherName;

    @JsonProperty("id-docente")
    private Long teacherId;

    @JsonProperty("codigo-unidade")
    private Integer unitCode;

    @JsonProperty("id-unidade")
    private Long unitId;

    @JsonProperty("nome-componente")
    private String componentName;

    @JsonProperty("codigo-componente")
    private String componentCode;

    @JsonProperty("codigo-turma")
    private String classCode;

    @JsonProperty("horario-turma")
    private String classSchedule;

    @JsonProperty("quantidade-discentes")
    private Integer studentCount;

    @JsonProperty("media-geral")
    private Double generalAverage;

    @JsonProperty("desvio-padrao-geral")
    private Double generalStandardDeviation;

    @JsonProperty("ano-turma")
    private Integer classYear;

    @JsonProperty("periodo-turma")
    private Integer classPeriod;

    private String cpf;
}
