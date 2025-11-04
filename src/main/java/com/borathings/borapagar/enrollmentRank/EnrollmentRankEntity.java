package com.borathings.borapagar.enrollmentRank;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "enrollment_rank")
@Data
public class EnrollmentRankEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer year;

    private Integer period;

    private Long classId;

    private Long studentId;

    private int rankPosition;

    private long priorityTypeId;

    private boolean reenrollment;

    private LocalDateTime processingTimestamp;

    private boolean uncertainRanking = false;
}
