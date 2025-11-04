package com.borathings.borapagar.enrollmentRank.dto;

import com.borathings.borapagar.classroom.dto.ClassroomDTO;
import com.borathings.borapagar.component.dto.ComponentResponseDTO;
import com.borathings.borapagar.enrollmentRank.EnrollmentRankEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EnrollmentResponseDTO {
    @JsonProperty("ano")
    private int year;

    @JsonProperty("prioridade")
    private long priorityTypeId;

    @JsonProperty("id-turma")
    private long classId;

    @JsonProperty("periodo")
    private int period;

    @JsonProperty("rematricula")
    private boolean isReEnrollment;

    @JsonProperty("capacidade")
    private int capacity;

    @JsonProperty("componente")
    private ComponentResponseDTO component;

    @JsonProperty("data_processamento")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS")
    private LocalDateTime processingTimestamp;

    @JsonProperty("rank")
    private int rankPosition;

    @JsonProperty("incerto")
    private boolean isUncertain;

    @JsonProperty("concorrencia")
    private Integer concorrence;

    public EnrollmentResponseDTO(
            int year,
            int period,
            int concorrence,
            EnrollmentRankEntity enrollmentRank,
            ClassroomDTO classroomDTO,
            ComponentResponseDTO componentDTO) {
        this.year = year;
        this.priorityTypeId = enrollmentRank.getPriorityTypeId();
        this.classId = enrollmentRank.getClassId();
        this.period = period;
        this.component = componentDTO;
        this.isReEnrollment = enrollmentRank.isReenrollment();
        this.capacity = classroomDTO.capacity();
        this.processingTimestamp = enrollmentRank.getProcessingTimestamp();
        this.rankPosition = enrollmentRank.getRankPosition();
        this.isUncertain = enrollmentRank.isUncertainRanking();
        this.concorrence = concorrence;
    }
}
