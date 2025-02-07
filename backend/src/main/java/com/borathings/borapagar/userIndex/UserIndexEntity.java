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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private IndexEnum indice;
    private String valor;
}
