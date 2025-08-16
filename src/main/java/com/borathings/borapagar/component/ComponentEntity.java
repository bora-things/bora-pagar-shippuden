package com.borathings.borapagar.component;

import com.borathings.borapagar.core.persistence.AbstractModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity(name = "components")
@Table(
        name = "components",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"code", "curricular_matrix_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString
public class ComponentEntity extends AbstractModel {

    @Column(name = "total_workload", nullable = false)
    private Integer totalWorkload;

    @Column(name = "co_requisites", nullable = false)
    private String coRequisites;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "mandatory_subject", nullable = false)
    private Boolean mandatorySubject;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "equivalent", nullable = false)
    private String equivalent;

    @Column(name = "component_id", nullable = false)
    private Integer componentId;

    @Column(name = "curricular_matrix_id", nullable = false)
    private Integer curricularMatrixId;

    @Column(name = "unit_id", nullable = false)
    private Integer unitId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "pre_requisites", nullable = false)
    private String preRequisites;
}
