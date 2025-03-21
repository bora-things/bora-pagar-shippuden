package com.borathings.borapagar.student.interest;

import com.borathings.borapagar.core.AbstractModel;
import com.borathings.borapagar.student.StudentEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class StudentSubjectInterestEntity extends AbstractModel {
    private Integer year;
    private Integer period;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    private int sigaaSubjectId;
}
