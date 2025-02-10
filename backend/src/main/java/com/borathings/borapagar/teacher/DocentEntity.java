package com.borathings.borapagar.teacher;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.core.AbstractModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "docents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class DocentEntity extends AbstractModel {

    @Column(name = "position")
    @NotNull
    private String position;

    @Column(name = "cpf")
    @NotNull
    private String cpf;

    @Column(name = "admission_date")
    @NotNull
    private Long admissionDate;

    @Column(name = "siape_digit")
    @NotNull // REVISIT TYPE
    private String siapeDigit;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "is_active")
    @NotNull
    private Integer isActive;

    @Column(name = "position_id")
    @NotNull
    private Integer positionId;

    @Column(name = "teacher_id")
    @NotNull
    private Integer teacherId;

    @Column(name = "institutional_id")
    @NotNull
    private Integer institutionalId;

    @Column(name = "status_id")
    @NotNull
    private Integer statusId;

    @Column(name = "unit_id")
    @NotNull
    private Integer unitId;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "identification_name")
    @NotNull
    private String identificationName;

    @Column(name = "work_regime")
    @NotNull
    private Integer workRegime;

    @Column(name = "gender")
    @NotNull
    private String gender;

    @Column(name = "siape")
    @NotNull
    private Integer siape;

    @Column(name = "unit")
    @NotNull
    private String unit;

    @OneToMany(mappedBy = "docent")
    private List<ClassroomEntity> classrooms;
}
