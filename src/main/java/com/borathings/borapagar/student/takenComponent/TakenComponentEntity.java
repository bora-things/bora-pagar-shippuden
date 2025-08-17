package com.borathings.borapagar.student.takenComponent;

import com.borathings.borapagar.core.persistence.AbstractModel;
import com.borathings.borapagar.student.StudentEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "taken_components",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"year", "student_id", "component_id", "period"})})
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TakenComponentEntity extends AbstractModel {
    @Column(name = "period", nullable = false)
    private Integer period;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "component_id")
    private Integer componentId;

    @Column(name = "situation")
    private Integer situation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", updatable = false)
    private StudentEntity student;
}
