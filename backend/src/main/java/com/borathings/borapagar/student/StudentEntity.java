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
    private int studentId;

    @Column(name = "student_name", nullable = false)
    @NotNull
    private String studentName;

    @Column(name = "enrollment_id", nullable = false)
    @NotNull
    private String enrollmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "situation", nullable = false)
    @NotNull
    private StudentSituation studentSituation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @NotNull
    private StudentType studentType;

    @Column(name = "course_id", nullable = false)
    @NotNull
    private int courseId;

    @Column(name = "course_name", nullable = false)
    @NotNull
    private String courseName;

    @Column(name = "level", nullable = false)
    @NotNull
    private String level;

    @Column(name = "admission_year", nullable = false)
    @NotNull
    private int admissionYear;

    @Column(name = "admission_semester", nullable = false)
    @NotNull
    private int admissionSemester;

    @Column(name = "ingress_method_id", nullable = false)
    @NotNull
    private int ingressMethodId;

    @Column(name = "ingress_method_description", nullable = false)
    @NotNull
    private String ingressMethodDescription;

    @Column(name = "academic_manager_id", nullable = false)
    @NotNull
    private int academicManagerId;

    @Column(name = "participant_type_id", nullable = false)
    @NotNull
    private int participantTypeId;

    @Column(name = "educational_institution_id", nullable = false)
    @NotNull
    private int educationalInstitutionId;

    @Column(name = "educational_institution", nullable = false)
    @NotNull
    private String educationalInstitution;

    @Column(name = "id_polo", nullable = false)
    @NotNull
    private int idPolo;

    @NotNull
    private String polo;

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
