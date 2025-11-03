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

    @Column(nullable = false)
    private Integer totalWorkload;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String coRequisites;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Boolean mandatorySubject;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String equivalent;

    @Column(nullable = false)
    private Integer componentId;

    @Column(nullable = false)
    private Integer curricularMatrixId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String preRequisites;
}
