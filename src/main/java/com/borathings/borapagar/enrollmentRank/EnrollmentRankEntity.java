package com.borathings.borapagar.enrollmentRank;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment_rank")
@Data
public class EnrollmentRankEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "component_code", nullable = false)
    private String componentCode;

    @Column(name = "class_id", nullable = false)
    private Long classId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "rank_position", nullable = false)
    private int rankPosition; // A posição do aluno na fila (1, 2, 3...)

    @Column(name = "priority_type_id", nullable = false)
    private long priorityTypeId;

    // Opcional: Guardar mais dados para referência
    @Column(name = "component_name")
    private String componentName;

    @Column(name = "enrollment_component_id")
    private long enrollmentComponentId;

    @Column(name = "processing_timestamp", nullable = false)
    private LocalDateTime processingTimestamp;

    @Column(name = "uncertain_ranking", nullable = false)
    private boolean uncertainRanking = false;

}

