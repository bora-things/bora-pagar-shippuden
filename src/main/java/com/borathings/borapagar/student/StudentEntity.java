package com.borathings.borapagar.student;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.core.SoftDeletableModel;
import com.borathings.borapagar.student.IdMappers.StudentSituation;
import com.borathings.borapagar.student.IdMappers.StudentType;
import com.borathings.borapagar.student.transcript.TranscriptComponentEntity;
import com.borathings.borapagar.user.UserEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    @Version
    private Long version;

    @NaturalId
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "student_name", nullable = false)
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
    private String level;

    @Column(name = "admission_year")
    private int admissionYear;

    @Column(name = "admission_semester")
    private int admissionSemester;

    @Column(name = "ingress_method_id")
    private int ingressMethodId;

    @Column(name = "ingress_method_description")
    private String ingressMethodDescription;

    @Column(name = "academic_manager_id")
    private int academicManagerId;

    @Column(name = "participant_type_id")
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

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TranscriptComponentEntity> transcriptComponents = new ArrayList<>();

    public int getUserPeriod() {
        Set<String> periods = new HashSet<>();

        for (TranscriptComponentEntity transcriptComponentEntity : transcriptComponents) {
            String periodKey = transcriptComponentEntity.getYear() + "-" + transcriptComponentEntity.getPeriod();
            periods.add(periodKey);
        }

        return periods.size();
    }
}
