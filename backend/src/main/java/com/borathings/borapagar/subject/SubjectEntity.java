package com.borathings.borapagar.subject;

import com.borathings.borapagar.core.AbstractModel;
import com.borathings.borapagar.subject_course.SubjectCourseEntity;
import io.micrometer.common.lang.Nullable;
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
@Table(name = "subject")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SubjectEntity extends AbstractModel {
    @Column @NotNull private String name;
    @Column @NotNull private String code;
    @Column @Nullable private String syllabus;
    @Column @NotNull private Integer hours;

    @OneToMany(mappedBy = "subject")
    private List<SubjectCourseEntity> courses;
}
