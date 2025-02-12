package com.borathings.borapagar.subject;

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

@Entity(name = "components")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ComponentEntity extends AbstractModel {

    @Column(name = "total_workload", nullable = false)
    @NotNull
    private Integer totalWorkload;

    @Column(name = "co_requisites", nullable = false)
    @NotNull
    private String coRequisites;

    @Column(name = "code", nullable = false)
    @NotNull
    private String code;

    @Column(name = "block_components")
    private String blockComponents;

    @Column(name = "department", nullable = false)
    @NotNull
    private String department;

    @Column(name = "activity_type_description", nullable = false)
    @NotNull
    private String activityTypeDescription;

    @Column(name = "mandatory_subject", nullable = false)
    @NotNull
    private Boolean mandatorySubject;

    @Column(name = "description", nullable = false)
    @NotNull
    private String description;

    @Column(name = "equivalent", nullable = false)
    @NotNull
    private String equivalent;

    @Column(name = "component_id", nullable = false)
    @NotNull
    private Integer componentId;

    @Column(name = "curricular_matrix_id", nullable = false)
    @NotNull
    private Integer curricularMatrixId;

    @Column(name = "activity_type_id", nullable = false)
    @NotNull
    private Integer activityTypeId;

    @Column(name = "component_type_id", nullable = false)
    @NotNull
    private Integer componentTypeId;

    @Column(name = "unit_id", nullable = false)
    @NotNull
    private Integer unitId;

    @Column(name = "level", nullable = false)
    @NotNull
    private String level;

    @Column(name = "name", nullable = false)
    @NotNull
    private String name;

    @Column(name = "number_units", nullable = false)
    @NotNull
    private Integer numberUnits;

    @Column(name = "pre_requisites", nullable = false)
    @NotNull
    private String preRequisites;

    @Column(name = "offered_semester", nullable = false)
    @NotNull
    private Integer offeredSemester;

    @OneToMany(mappedBy = "component")
    private List<ClassroomEntity> classes;
}
