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
    @Column @NotNull private String email;
    @Column @NotNull private String name;
    @Column @NotNull @NaturalId private String googleId;
    @Column private String imageUrl;
    @Column @NotNull private boolean profileComplete;
    @Column private Integer current_period;
    @Column private Float iea;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity other = (UserEntity) o;
        return Objects.equals(getGoogleId(), other.getGoogleId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getGoogleId());
    }
}
