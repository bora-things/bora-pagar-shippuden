package com.borathings.borapagar.teacher;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.core.AbstractModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity(name = "docentes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class DocenteEntity extends AbstractModel {

    @Column(name = "cargo") @NotNull
    private String cargo;

    @Column(name = "cpf") @NotNull
    private String cpf;

    @Column(name = "data_admissao") @NotNull
    private Long dataAdmissao;

    @Column(name = "digito_siape") @NotNull //REVER TIPO
    private String digitoSiape;

    @Column(name = "email") @NotNull
    private String email;

    @Column(name = "id_ativo") @NotNull
    private Integer idAtivo;

    @Column(name = "id_cargo") @NotNull
    private Integer idCargo;

    @Column(name = "id_docente") @NotNull
    private Integer idDocente;

    @Column(name = "id_institucional") @NotNull
    private Integer idInstitucional;

    @Column(name = "id_situacao") @NotNull
    private Integer idSituacao;

    @Column(name = "id_unidade") @NotNull
    private Integer idUnidade;

    @Column(name = "nome") @NotNull
    private String nome;

    @Column(name = "nome_identificacao") @NotNull
    private String nomeIdentificacao;

    @Column(name = "regime_trabalho") @NotNull
    private Integer regimeTrabalho;

    @Column(name = "sexo") @NotNull
    private String sexo;

    @Column(name = "siape") @NotNull
    private Integer siape;

    @Column(name = "unidade") @NotNull
    private String unidade;

    @OneToMany(mappedBy = "docente")
    private List<ClassroomEntity> turmas;
}
