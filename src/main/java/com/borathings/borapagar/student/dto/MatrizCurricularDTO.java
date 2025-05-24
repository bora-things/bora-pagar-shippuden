package com.borathings.borapagar.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MatrizCurricularDTO(
        int ano,
        boolean ativo,
        @JsonProperty("ch-complementar-minima") int chComplementarMinima,
        @JsonProperty("ch-optativas-minima") int chOptativasMinima,
        @JsonProperty("ch-total-minima") int chTotalMinima,
        @JsonProperty("codigo-curriculo") String codigoCurriculo,
        @JsonProperty("creditos-ideal-semestre") int creditosIdealSemestre,
        @JsonProperty("creditos-maximo-semestre") int creditosMaximoSemestre,
        @JsonProperty("creditos-minimo-semestre") int creditosMinimoSemestre,
        String enfase,
        @JsonProperty("id-curso") int idCurso,
        @JsonProperty("id-grau-academico") int idGrauAcademico,
        @JsonProperty("id-matriz-curricular") int idMatrizCurricular,
        @JsonProperty("id-modalidade-educacao") int idModalidadeEducacao,
        String municipio,
        @JsonProperty("nome-curso") String nomeCurso,
        int periodo,
        @JsonProperty("semestre-conclusao-ideal") int semestreConclusaoIdeal,
        @JsonProperty("semestre-conclusao-maximo") int semestreConclusaoMaximo,
        @JsonProperty("semestre-conclusao-minimo") int semestreConclusaoMinimo,
        String turno) {}
