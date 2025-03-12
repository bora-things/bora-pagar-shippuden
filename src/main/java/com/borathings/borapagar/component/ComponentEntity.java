package com.borathings.borapagar.component;

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

    @Column(name = "total_workload")
    private Integer totalWorkload;

    @Column(name = "co_requisites")
    private String coRequisites;

    @Column(name = "code")
    private String code;

    @Column(name = "block_components")
    private String blockComponents;

    @Column(name = "department")
    private String department;

    @Column(name = "activity_type_description")
    private String activityTypeDescription;

    @Column(name = "mandatory_subject")
    private Boolean mandatorySubject;

    @Column(name = "description")
    private String description;

    @Column(name = "equivalent")
    private String equivalent;

    @Column(name = "component_id")
    private Integer componentId;

    @Column(name = "curricular_matrix_id")
    private Integer curricularMatrixId;

    @Column(name = "activity_type_id")
    private Integer activityTypeId;

    @Column(name = "component_type_id")
    private Integer componentTypeId;

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "level")
    private String level;

    @Column(name = "name")
    private String name;

    @Column(name = "number_units")
    private Integer numberUnits;

    @Column(name = "pre_requisites")
    private String preRequisites;

    @Column(name = "offered_semester")
    private Integer offeredSemester;

    @OneToMany(mappedBy = "component")
    private List<ClassroomEntity> classes;
}
