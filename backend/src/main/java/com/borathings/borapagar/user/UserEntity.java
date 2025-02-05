package com.borathings.borapagar.user;

import com.borathings.borapagar.core.SoftDeletableModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NaturalId;

/** UserEntity */
@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserEntity extends SoftDeletableModel {
    @Column
    @NotNull
    private String email;

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    @NaturalId
    private String idUsuario;

    @Column
    @NotNull
    private String idDiscente;

    @Column
    @NotNull
    private String idInstitucional;

    @Column
    @NotNull
    private String cpf;

    @Column
    private String imageUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity other = (UserEntity) o;
        return Objects.equals(getIdUsuario(), other.getIdUsuario());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdUsuario());
    }
}
