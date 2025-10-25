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

    @Column(name="year" ,nullable = false)
    private Integer year;

    @Column(name="period",nullable = false)
    private Integer period;

    @Column(name = "class_id", nullable = false)
    private Long classId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "rank_position", nullable = false)
    private int rankPosition;

    @Column(name = "priority_type_id", nullable = false)
    private long priorityTypeId;

    @Column(name = "processing_timestamp", nullable = false)
    private LocalDateTime processingTimestamp;

    @Column(name = "uncertain_rank", nullable = false)
    private boolean uncertainRanking = false;

}

