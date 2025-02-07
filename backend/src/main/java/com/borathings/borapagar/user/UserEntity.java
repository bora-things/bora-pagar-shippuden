package com.borathings.borapagar.user;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.core.SoftDeletableModel;
import com.borathings.borapagar.userIndex.UserIndexEntity;
import jakarta.persistence.*;
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
    @Column(name = "email", nullable = false)
    @NotNull
    private String email;

    @Column(name = "nome", nullable = false)
    @NotNull
    private String name;

    @Column(name = "login", nullable = false)
    @NotNull
    private String login;

    @Column(name = "id_usuario", nullable = false, unique = true)
    @NotNull
    @NaturalId
    private int idUsuario;

    @Column(name = "id_discente", nullable = false)
    @NotNull
    private int idDiscente;

    @Column(name = "id_institucional", nullable = false)
    @NotNull
    private Long idInstitucional;

    @Column(name = "cpf", nullable = false)
    @NotNull
    private Long cpf;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "id_unidade", nullable = false)
    @NotNull
    private int idUnidade;

    @Column(name = "ativo", nullable = false)
    @NotNull
    private boolean ativo;

    @Column(name = "matricula", nullable = false)
    @NotNull
    private String matricula;

    @Column(name = "id_situacao_discente", nullable = false)
    @NotNull
    private int idSituacaoDiscente;

    @Column(name = "id_tipo_discente", nullable = false)
    @NotNull
    private int idTipoDiscente;

    @Column(name = "id_curso", nullable = false)
    @NotNull
    private int idCurso;

    @Column(name = "nome_curso", nullable = false)
    @NotNull
    private String nomeCurso;

    @Column(name = "sigla_nivel", nullable = false)
    @NotNull
    private String siglaNivel;

    @Column(name = "ano_ingresso", nullable = false)
    @NotNull
    private int anoIngresso;

    @Column(name = "periodo_ingresso", nullable = false)
    @NotNull
    private int periodoIngresso;

    @Column(name = "id_forma_ingresso", nullable = false)
    @NotNull
    private int idFormaIngresso;

    @Column(name = "descricao_forma_ingresso", nullable = false)
    @NotNull
    private String descricaoFormaIngresso;

    @Column(name = "id_gestora_academica", nullable = false)
    @NotNull
    private int idGestoraAcademica;

    @Column(name = "id_tipo_participante", nullable = false)
    @NotNull
    private int idTipoParticipante;

    @Column(name = "id_instituicao_ensino", nullable = false)
    @NotNull
    private int idInstituicaoEnsino;

    @Column(name = "instituicao_ensino", nullable = false)
    @NotNull
    private String instituicaoEnsino;

    @Column(name = "id_polo", nullable = false)
    @NotNull
    private int idPolo;

    @Column(name = "polo", nullable = false)
    @NotNull
    private String polo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassroomEntity> classrooms = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private UserIndexEntity index;

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
