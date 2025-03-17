package com.borathings.borapagar.docent;

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
    private String position;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "admission_date")
    private Long admissionDate;

    @Column(name = "siape_digit")
    private String siapeDigit;

    @Column(name = "email")
    private String email;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "position_id")
    private Integer positionId;

    @Column(name = "teacher_id")
    private Integer teacherId;

    @Column(name = "institutional_id")
    private Integer institutionalId;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "name")
    private String name;

    @Column(name = "identification_name")
    private String identificationName;

    @Column(name = "work_regime")
    private Integer workRegime;

    @Column(name = "gender")
    private String gender;

    @Column(name = "siape")
    private Integer siape;

    @Column(name = "unit")
    private String unit;

    @OneToMany(mappedBy = "docent")
    private List<ClassroomEntity> classrooms;
}
