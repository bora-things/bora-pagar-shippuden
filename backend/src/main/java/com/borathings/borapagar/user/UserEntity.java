package com.borathings.borapagar.user;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.core.SoftDeletableModel;
import com.borathings.borapagar.userIndex.UserIndexEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
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
    private String login;

    @Column
    @NotNull
    @NaturalId
    private int idUsuario;

    @Column
    @NotNull
    private int idDiscente;

    @Column
    @NotNull
    private Long idInstitucional;

    @Column
    @NotNull
    private Long cpf;

    @Column
    private String imageUrl;

    @Column
    @NotNull
    private int idUnidade;

    @Column
    @NotNull
    private boolean ativo;

    @Column
    @NotNull
    private String matricula;

    @Column
    @NotNull
    private int idSituacaoDiscente;

    @Column
    @NotNull
    private int idTipoDiscente;

    @Column
    @NotNull
    private int idCurso;

    @Column
    @NotNull
    private String nomeCurso;

    @Column
    @NotNull
    private String siglaNivel;

    @Column
    @NotNull
    private int anoIngresso;

    @Column
    @NotNull
    private int periodoIngresso;

    @Column
    @NotNull
    private int idFormaIngresso;

    @Column
    @NotNull
    private String descricaoFormaIngresso;

    @Column
    @NotNull
    private int idGestoraAcademica;

    @Column
    @NotNull
    private int idTipoParticipante;

    @Column
    @NotNull
    private int idInstituicaoEnsino;

    @Column
    @NotNull
    private String instituicaoEnsino;

    @Column
    @NotNull
    private int idPolo;

    @Column
    @NotNull
    private String polo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassroomEntity> classrooms = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserIndexEntity> indexes = new ArrayList<>();

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
