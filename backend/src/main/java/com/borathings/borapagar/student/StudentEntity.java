package com.borathings.borapagar.student;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.core.SoftDeletableModel;
import com.borathings.borapagar.student.IdMappers.StudentSituation;
import com.borathings.borapagar.student.IdMappers.StudentType;
import com.borathings.borapagar.student.index.StudentIndexEntity;
import com.borathings.borapagar.user.UserEntity;
import com.borathings.borapagar.workload.WorkloadEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NaturalId;

@Entity(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class StudentEntity extends SoftDeletableModel {

    @NaturalId
    @Column(name = "student_id", nullable = false)
    @NotNull
    private Long studentId;

    @Column(name = "student_name", nullable = false)
    @NotNull
    private String studentName;

    @Column(name = "enrollment_id")
    private String enrollmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "situation")
    private StudentSituation studentSituation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private StudentType studentType;

    @Column(name = "course_id")
    private int courseId;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "level")
    @NotNull
    private String level;

    @Column(name = "admission_year")
    @NotNull
    private int admissionYear;

    @Column(name = "admission_semester")
    @NotNull
    private int admissionSemester;

    @Column(name = "ingress_method_id")
    private int ingressMethodId;

    @Column(name = "ingress_method_description")
    private String ingressMethodDescription;

    @Column(name = "academic_manager_id")
    @NotNull
    private int academicManagerId;

    @Column(name = "participant_type_id")
    @NotNull
    private int participantTypeId;

    @Column(name = "educational_institution_id")
    private int educationalInstitutionId;

    @Column(name = "educational_institution")
    private String educationalInstitution;

    @Column(name = "id_polo")
    private int campusId;

    @Column(name = "campus")
    private String campus;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassroomEntity> classrooms = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "index_id", unique = true)
    private StudentIndexEntity indexes;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "workload_id", unique = true)
    private WorkloadEntity workload;
}
