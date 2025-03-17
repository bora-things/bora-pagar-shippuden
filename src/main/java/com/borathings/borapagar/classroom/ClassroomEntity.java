package com.borathings.borapagar.classroom;

import com.borathings.borapagar.core.AbstractModel;
import com.borathings.borapagar.docent.DocentEntity;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.subject.ComponentEntity;
import jakarta.persistence.*;
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
    private Long classroomId;

    @Column(nullable = false)
    private int year;

    @Column(name = "student_capacity", nullable = false)
    private int studentCapacity;

    @Column(name = "component_code", nullable = false)
    private String componentCode;

    @Column(name = "classroom_code", nullable = false)
    private String classroomCode;

    @Column(name = "schedule_description")
    private String scheduleDescription;

    @Column(name = "external_teacher_id")
    private Long externalTeacherId;

    @Column(name = "education_mode_id")
    private Long educationModeId;

    @Column(name = "classroom_status_id")
    private Long classroomStatusId;

    @Column(name = "grouping_classroom_id")
    private Long groupingClassroomId;

    @Column(name = "unit_id")
    private Long unitId;

    @Column
    private String location;

    @Column(name = "component_name")
    private String componentName;

    @Column(nullable = false)
    private int semester;

    @Column(name = "level_abbreviation")
    private String levelAbbreviation;

    @Column(nullable = false)
    private boolean subgroup;

    @Column(nullable = false)
    private int type;

    @Column(name = "uses_new_virtual_classroom", nullable = false)
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
