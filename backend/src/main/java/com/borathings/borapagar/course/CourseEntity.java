package com.borathings.borapagar.course;

import com.borathings.borapagar.core.AbstractModel;
import com.borathings.borapagar.subject_course.SubjectCourseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "course")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CourseEntity extends AbstractModel {
    @Column @NotNull private String name;
    @Column @NotNull private String coordinator;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<SubjectCourseEntity> subjects;
}
