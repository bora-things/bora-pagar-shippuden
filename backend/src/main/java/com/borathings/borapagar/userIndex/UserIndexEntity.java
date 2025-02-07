package com.borathings.borapagar.userIndex;

import com.borathings.borapagar.core.AbstractModel;
import com.borathings.borapagar.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity(name = "indices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserIndexEntity extends AbstractModel {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
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
