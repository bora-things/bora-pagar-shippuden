package com.borathings.borapagar.classroom;

import com.borathings.borapagar.core.persistence.AbstractModel;
import com.borathings.borapagar.student.StudentEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity(name = "classrooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "classrooms",
        uniqueConstraints = {@UniqueConstraint(columnNames = {("classroom_id")})})
@SuperBuilder(toBuilder = true)
public class ClassroomEntity extends AbstractModel {

    @Column(name = "classroom_id", nullable = false, unique = true)
    private Long classroomId;

    @Column(nullable = false)
    private int year;

    @Column(name = "component_code", nullable = false)
    private String componentCode;

    @Column(name = "classroom_code", nullable = false)
    private String classroomCode;

    @Column(name = "unit_id")
    private Long unitId;

    @Column
    private String location;

    @Column(name = "component_name")
    private String componentName;

    @Column(nullable = false)
    private int period;

    @Column(name = "level_abbreviation")
    private String levelAbbreviation;

    @Column(nullable = false)
    private boolean subgroup;

    @Column(nullable = false)
    private int type;

    @ManyToMany(mappedBy = "classrooms")
    private List<StudentEntity> students = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassroomEntity that)) return false;
        return Objects.equals(classroomId, that.classroomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classroomId);
    }
}
