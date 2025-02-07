package com.borathings.borapagar.subject;

import com.borathings.borapagar.classroom.ClassroomEntity;
import com.borathings.borapagar.core.AbstractModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity(name = "componentes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class SubjectEntity extends AbstractModel {

    @Column(name = "carga_horaria_total")
    private Integer cargaHorariaTotal;

    @Column(name = "co_requisitos")
    private String coRequisitos;

    @Column(name = "codigo")
    private String codigo;

    @ElementCollection
    @CollectionTable(name = "componentes_bloco")
    private List<String> componentesBloco; // Ajustado para lista de strings (se necessário, pode mudar)

    @Column(name = "departamento")
    private String departamento;

    @Column(name = "descricao_tipo_atividade")
    private String descricaoTipoAtividade;

    @Column(name = "disciplina_obrigatoria")
    private Boolean disciplinaObrigatoria;

    @Column(name = "ementa")
    private String ementa;

    @Column(name = "equivalentes")
    private String equivalentes;

    @Column(name = "id_componente")
    private Integer idComponente;

    @Column(name = "id_matriz_curricular")
    private Integer idMatrizCurricular;

    @Column(name = "id_tipo_atividade")
    private Integer idTipoAtividade;

    @Column(name = "id_tipo_componente")
    private Integer idTipoComponente;

    @Column(name = "id_unidade")
    private Integer idUnidade;

    @Column(name = "nivel")
    private String nivel;

    @Column(name = "nome")
    private String nome;

    @Column(name = "num_unidades")
    private Integer numUnidades;

    @Column(name = "pre_requisitos")
    private String preRequisitos;

    @Column(name = "semestre_oferta")
    private Integer semestreOferta;

    @OneToMany(mappedBy = "componente")
    private List<ClassroomEntity> turmas;

}
