package com.borathings.borapagar.student.transcript;

import com.borathings.borapagar.core.AbstractModel;
import com.borathings.borapagar.student.StudentEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "transcript_components")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptComponentEntity extends AbstractModel {
    @Column(name = "absences", nullable = false)
    private Integer absences;

    @Column(name = "register_date")
    private Long registerDate;

    @Column(name = "period", nullable = false)
    private Integer period;

    @Column(name = "year", nullable = false)
    @NotNull
    private Integer year;

    @Column(name = "component_id")
    private Long componentId;

    @Column(name = "situation")
    private String situation;

    @Column(name = "integralization")
    private String integralization;

    @Column(name = "sigaa_class_id", nullable = false)
    private Integer sigaaClassId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private StudentEntity student;
}
