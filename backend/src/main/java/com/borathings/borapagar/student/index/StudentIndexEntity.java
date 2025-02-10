package com.borathings.borapagar.student.index;

import com.borathings.borapagar.core.AbstractModel;
import com.borathings.borapagar.student.StudentEntity;
import com.borathings.borapagar.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "indexes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class StudentIndexEntity extends AbstractModel {

    @OneToOne
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;
    private String mc;
    private String ira;
    private String mcn;
    private String iech;
    private String iepl;
    private String iea;
    private String iean;
    private String cr;
    private String ispl;
    private String iechp;
}
