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

    private Long classroomId;

    private int year;

    private String componentCode;

    private String classroomCode;

    private Long unitId;

    private String componentName;

    @Column(nullable = false)
    private int period;

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
