package com.borathings.borapagar.classroom;

import com.borathings.borapagar.core.AbstractModel;

import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.subject.ComponentEntity;
import com.borathings.borapagar.docent.DocentEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity(name = "classrooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ClassroomEntity extends AbstractModel {

    @Column(name = "classroom_id", nullable = false, unique = true)
    @NotNull
    private Long classroomId;

    @Column(nullable = false)
    @NotNull
    private int year;

    @Column(name = "student_capacity", nullable = false)
    @NotNull
    private int studentCapacity;

    @Column(name = "component_code", nullable = false)
    @NotNull
    private String componentCode;

    @Column(name = "classroom_code", nullable = false)
    @NotNull
    private String classroomCode;

    @Column(name = "schedule_description")
    @NotNull
    private String scheduleDescription;

    @Column(name = "external_teacher_id")
    @NotNull
    private Long externalTeacherId;

    @Column(name = "education_mode_id")
    @NotNull
    private Long educationModeId;

    @Column(name = "classroom_status_id")
    @NotNull
    private Long classroomStatusId;

    @Column(name = "grouping_classroom_id")
    @NotNull
    private Long groupingClassroomId;

    @Column(name = "unit_id")
    @NotNull
    private Long unitId;

    @Column
    @NotNull
    private String location;

    @Column(name = "component_name")
    @NotNull
    private String componentName;

    @Column(nullable = false)
    @NotNull
    private int semester;

    @Column(name = "level_abbreviation")
    @NotNull
    private String levelAbbreviation;

    @Column(nullable = false)
    @NotNull
    private boolean subgroup;

    @Column(nullable = false)
    @NotNull
    private int type;

    @Column(name = "uses_new_virtual_classroom", nullable = false)
    @NotNull
    private boolean usesNewVirtualClassroom;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "docent_id")
    private DocentEntity docent;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "component_id")
    private ComponentEntity component;
}
