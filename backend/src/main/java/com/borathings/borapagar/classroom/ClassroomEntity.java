package com.borathings.borapagar.classroom;

import com.borathings.borapagar.core.AbstractModel;
import com.borathings.borapagar.subject.SubjectEntity;
import com.borathings.borapagar.teacher.DocentEntity;
import com.borathings.borapagar.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity(name = "turma")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ClassroomEntity extends AbstractModel {

    @Column(name = "id_turma", nullable = false, unique = true)
    private Long idTurma;

    @Column(nullable = false)
    private int ano;

    @Column(name = "capacidade_aluno", nullable = false)
    private int capacidadeAluno;

    @Column(name = "codigo_componente", nullable = false)
    private String codigoComponente;

    @Column(name = "codigo_turma", nullable = false)
    private String codigoTurma;

    @Column(name = "descricao_horario")
    private String descricaoHorario;

    @Column(name = "id_discente")
    private Long idDiscente;

    @Column(name = "id_docente_externo")
    private Long idDocenteExterno;

    @Column(name = "id_modalidade_educacao")
    private Long idModalidadeEducacao;

    @Column(name = "id_situacao_turma")
    private Long idSituacaoTurma;

    @Column(name = "id_turma_agrupadora")
    private Long idTurmaAgrupadora;

    @Column(name = "id_unidade")
    private Long idUnidade;

    @Column
    private String local;

    @Column(name = "nome_componente")
    private String nomeComponente;

    @Column(nullable = false)
    private int periodo;

    @Column(name = "sigla_nivel")
    private String siglaNivel;

    @Column(nullable = false)
    private boolean subturma;

    @Column(nullable = false)
    private int tipo;

    @Column(name = "utiliza_nova_turma_virtual", nullable = false)
    private boolean utilizaNovaTurmaVirtual;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "docente_id")
    private DocentEntity docente;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "componente_id")
    private SubjectEntity componente;
}
