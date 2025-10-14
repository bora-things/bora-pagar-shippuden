package com.borathings.borapagar.student;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.core.persistence.SoftDeletableModel;
import com.borathings.borapagar.student.enums.StudentSituation;
import com.borathings.borapagar.student.interest.StudentSubjectInterestEntity;
import com.borathings.borapagar.student.takenComponent.TakenComponentEntity;
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

    @Column(name = "login", nullable = false)
    private String login;

    @Enumerated(EnumType.STRING)
    @Column(name = "situation")
    private StudentSituation studentSituation;

    @Column(name = "course_id")
    private int courseId;

    @Column(name = "curricular_matrix")
    private Integer curricularMatrix;

    @Column(name = "emphasis")
    private String emphasis;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "course_name")
    private String courseName;

    @ManyToMany
    @JoinTable(
            name = "student_classroom",
            joinColumns = @JoinColumn(name = "student_id"), // chave de Student
            inverseJoinColumns = @JoinColumn(name = "classroom_id") // chave de Classroom
            )
    private Set<ClassroomEntity> classrooms = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentSubjectInterestEntity> interests = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TakenComponentEntity> takenComponents = new ArrayList<>();

    public int getUserPeriod() {
        Set<String> periods = new HashSet<>();

        for (ClassroomEntity classrooms : classrooms) {
            if (classrooms.getPeriod() != 1 && classrooms.getPeriod() != 2) {
                continue;
            }
            String periodKey = classrooms.getYear() + "-" + classrooms.getPeriod();
            periods.add(periodKey);
        }

        return periods.size();
    }
}
